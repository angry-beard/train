package com.beard.train.framework.aop.support;

import com.beard.train.framework.aop.aspect.BeardAdvice;
import com.beard.train.framework.aop.config.BeardAopConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeardAdvisedSupport {

    private BeardAopConfig aopConfig;
    private Object target;
    private Class targetClass;
    private Pattern pointCutClassPattern;

    private Map<Method, Map<String, BeardAdvice>> methodCache;

    public BeardAdvisedSupport(BeardAopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    private void parse() {
        String pointCut = aopConfig.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", "\\\\*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        //三段
        //1、方法的修饰符和返回值
        //2、类名
        //3、方法的名称和形参列表
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf("" + 1)));
        methodCache = new HashMap<>();
        Pattern pointCutPattern = Pattern.compile(pointCut);
        try {
            Class aspectClass = Class.forName(this.aopConfig.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            for (Method method : this.targetClass.getMethods()) {
                String methodStr = method.toString();
                if (methodStr.contains("throws")) {
                    methodStr = methodStr.substring(0, methodStr.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pointCutPattern.matcher(methodStr);
                if (matcher.matches()) {
                    Map<String, BeardAdvice> advices = new HashMap<>();
                    if (null == aopConfig.getAspectBefore() || "".equals(aopConfig.getAspectBefore())) {
                        advices.put("before", new BeardAdvice(aspectClass.newInstance(), aspectMethods.get(aopConfig.getAspectBefore())));
                    }
                    if (null == aopConfig.getAspectAfter() || "".equals(aopConfig.getAspectAfter())) {
                        advices.put("after", new BeardAdvice(aspectClass.newInstance(), aspectMethods.get(aopConfig.getAspectAfter())));
                    }
                    if (null == aopConfig.getAspectAfterThrow() || "".equals(aopConfig.getAspectAfterThrow())) {
                        BeardAdvice advice = advices.put("afterThrow", new BeardAdvice(aspectClass.newInstance(), aspectMethods.get(aopConfig.getAspectAfterThrow())));
                        advice.setThrowName(aopConfig.getAspectAfterThrowingName());
                        advices.put("afterThrow", advice);
                    }
                    methodCache.put(method, advices);
                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public Map<String, BeardAdvice> getAdvices(Method method, Object o) throws NoSuchMethodException {
        Map<String, BeardAdvice> cache = methodCache.get(method);
        if (null == cache) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cache = methodCache.get(m);
            this.methodCache.put(m, cache);
        }
        return cache;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public void setTargetClass(Class<?> clazz) {
        this.targetClass = clazz;
        parse();
    }

    public void setTarget(Object instance) {
        this.target = instance;
    }
}
