package com.beard.train.framework.webmvc.servlet;

import com.beard.train.framework.annotation.BeardRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BeardHandlerAdapter {

    public BeardModelAndView handler(HttpServletRequest request, HttpServletResponse response, BeardHandlerMapping handlerMapping) throws Exception {
        //保存形参列表
        //将参数名称和参数位置 这种关系存起来
        Map<String, Integer> paramIndexMapping = new HashMap<>();
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof BeardRequestParam) {
                    String paramName = ((BeardRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        //初始化一下
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> paramType = paramTypes[i];
            if ((paramType == HttpServletRequest.class) || (paramType == HttpServletResponse.class)) {
                paramIndexMapping.put(paramType.getName(), i);
            }
        }
        //去拼接实参列表
        Map<String, String[]> params = request.getParameterMap();
        Object[] paramValues = new Object[paramTypes.length];
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(params.get(param.getKey()))
                    .replaceAll("\\[|\\]", "")
                    .replaceAll("\\s+", ",");
            if (!paramIndexMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramIndexMapping.get(param.getKey());
            //允许自定义的类型转换器
            paramValues[index] = castStringValue(value, paramTypes[index]);
        }
        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }
        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (result == null || result instanceof Void) {
            return null;
        }
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == BeardModelAndView.class;
        if (isModelAndView) {
            return (BeardModelAndView) result;
        }
        return null;
    }

    private Object castStringValue(String value, Class<?> paramType) {
        if (String.class == paramType) {
            return value;
        } else if (Integer.class == paramType) {
            return Integer.valueOf(value);
        } else if (Double.class == paramType) {
            return Double.valueOf(value);
        } else if (Objects.nonNull(value)) {
            return value;
        }
        return null;
    }
}
