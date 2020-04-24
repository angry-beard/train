package com.beard.train.framework.aop;

import com.beard.train.framework.aop.aspect.BeardAdvice;
import com.beard.train.framework.aop.support.BeardAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class JdkDynamicAopProxy implements InvocationHandler {

    private BeardAdvisedSupport config;

    public JdkDynamicAopProxy(BeardAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, BeardAdvice> advices = config.getAdvices(method, null);
        Object returnValue;
        try {
            invokeAdvice(advices.get("before"));

            returnValue = method.invoke(this.config.getTarget(), args);

            invokeAdvice(advices.get("after"));

        } catch (Exception e) {
            invokeAdvice(advices.get("afterThrow"));
            throw e;
        }
        return returnValue;
    }

    private void invokeAdvice(BeardAdvice advice) {
        try {
            advice.getAdviceMethod().invoke(advice.getAspect());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.config.getTargetClass().getInterfaces(), this);
    }
}
