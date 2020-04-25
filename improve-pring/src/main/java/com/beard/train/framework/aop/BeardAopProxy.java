package com.beard.train.framework.aop;

public interface BeardAopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
