package com.beard.train.framework.aop.intercept;

public interface BeardMethodInterceptor {

    Object invoke(BeardMethodInvocation mi) throws Throwable;
}
