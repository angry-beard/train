package com.beard.train.framework.aop.aspect;

import com.beard.train.framework.aop.intercept.BeardMethodInterceptor;
import com.beard.train.framework.aop.intercept.BeardMethodInvocation;

import java.lang.reflect.Method;

public class BeardAfterThrowingAdvice extends BeardAbstractAspectJAdvice implements BeardAdvice, BeardMethodInterceptor {

    private String throwingName;
    private BeardMethodInvocation mi;

    public BeardAfterThrowingAdvice(Object newInstance, Method method) {
        super(newInstance, method);
    }

    public void setThrowingName(String name) {
        this.throwingName = name;
    }

    @Override
    public Object invoke(BeardMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Exception e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }
}
