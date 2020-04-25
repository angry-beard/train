package com.beard.train.framework.aop.aspect;

import com.beard.train.framework.aop.BeardJoinPoint;

import java.lang.reflect.Method;

public abstract class BeardAbstractAspectJAdvice implements BeardAdvice {

    private Object aspectTarget;
    private Method adviceMethod;

    public BeardAbstractAspectJAdvice(Object newInstance, Method method) {
        this.aspectTarget = newInstance;
        this.adviceMethod = method;
    }

    protected Object invokeAdviceMethod(BeardJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] paramsTypes = this.adviceMethod.getParameterTypes();
        if (null == paramsTypes || paramsTypes.length == 0) {
            return this.adviceMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramsTypes.length];
            for (int i = 0; i < paramsTypes.length; i++) {
                if (paramsTypes[i] == BeardJoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramsTypes[i] == Throwable.class) {
                    args[i] = ex;
                } else if (paramsTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.adviceMethod.invoke(aspectTarget, args);
        }
    }
}
