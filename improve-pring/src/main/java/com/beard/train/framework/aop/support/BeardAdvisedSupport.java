package com.beard.train.framework.aop.support;

import com.beard.train.framework.aop.aspect.BeardAdvice;
import com.beard.train.framework.aop.aspect.BeardAfterReturningAdvice;
import com.beard.train.framework.aop.aspect.BeardAfterThrowingAdvice;
import com.beard.train.framework.aop.aspect.BeardBeforeAdvice;
import com.beard.train.framework.aop.config.BeardAopConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeardAdvisedSupport {

    private BeardAopConfig config;
    private Object target;
    private Class targetClass;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;

    public BeardAdvisedSupport(BeardAopConfig config) {
        this.config = config;
    }

    private void parse() throws Throwable {
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\*", "\\\\*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        //三段
        //1、方法的修饰符和返回值
        //2、类名
        //3、方法的名称和形参列表
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));
        methodCache = new HashMap<>();
        Pattern pointCutPattern = Pattern.compile(pointCut);
        try {
            Class aspectClass = Class.forName(this.config.getAspectClass());
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
                    List<Object> advices = new LinkedList<>();
                    if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore().trim()))) {
                        advices.add(new BeardBeforeAdvice(aspectMethods.get(config.getAspectBefore()), (Method) aspectClass.newInstance()));
                    }
                    if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter().trim()))) {
                        advices.add(new BeardAfterReturningAdvice(aspectMethods.get(config.getAspectAfter()), (Method) aspectClass.newInstance()));
                    }
                    if (!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow().trim()))) {
                        BeardAfterThrowingAdvice afterThrowingAdvice = new BeardAfterThrowingAdvice(aspectMethods.get(config.getAspectAfterThrow()), (Method) aspectClass.newInstance());
                        afterThrowingAdvice.setThrowingName(afterThrowingAdvice.toString());
                        advices.add(afterThrowingAdvice);
                    }
                    methodCache.put(method, advices);
                }
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cache = methodCache.get(method);
        if (cache == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cache = methodCache.get(m);
            this.methodCache.put(m, cache);
        }
        return cache;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public void setTargetClass(Class<?> clazz) throws Throwable {
        this.targetClass = clazz;
        parse();
    }

    public void setTarget(Object instance) {
        this.target = instance;
    }
}
