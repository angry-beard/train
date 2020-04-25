package com.beard.train.framework.aop;

import com.beard.train.framework.aop.intercept.BeardMethodInvocation;
import com.beard.train.framework.aop.support.BeardAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements BeardAopProxy, InvocationHandler {

    private BeardAdvisedSupport config;

    public JdkDynamicAopProxy(BeardAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> advices = config.getInterceptorsAndDynamicInterceptionAdvice(method, this.config.getTargetClass());
        BeardMethodInvocation methodInvocation = new BeardMethodInvocation(proxy, this.config.getTarget(), method, args, this.config.getTargetClass(), advices);
        return methodInvocation.proceed();
    }

    public Object getProxy() {
        return getProxy(this.getClass().getClassLoader());

    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }
}
