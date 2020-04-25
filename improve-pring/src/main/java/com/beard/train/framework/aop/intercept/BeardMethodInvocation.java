package com.beard.train.framework.aop.intercept;

import com.beard.train.framework.aop.BeardJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeardMethodInvocation implements BeardJoinPoint {

    private Object proxy;
    private Method method;
    private Object target;
    private Class<?> targetClass;
    private Object[] arguments;
    private List<Object> advices;

    private Map<String, Object> userAttributes;
    private int currentInterceptorIndex = -1;

    public BeardMethodInvocation(Object proxy, Object target, Method method, Object[] args, Class targetClass, List<Object> advices) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.arguments = args;
        this.advices = advices;
    }

    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.advices.size() - 1) {
            return this.method.invoke(this.target, this.arguments);
        }
        Object advice = advices.get(++this.currentInterceptorIndex);
        if (advice instanceof BeardMethodInterceptor) {
            BeardMethodInterceptor interceptor = (BeardMethodInterceptor) advice;
            return interceptor.invoke(this);
        } else {
            return proceed();
        }
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (null != value) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.userAttributes != null ? this.userAttributes.get(key) : null);
    }
}
