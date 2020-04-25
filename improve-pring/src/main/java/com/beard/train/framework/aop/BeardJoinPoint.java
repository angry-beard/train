package com.beard.train.framework.aop;

import java.lang.reflect.Method;

public interface BeardJoinPoint {

    Method getMethod();

    Object[] getArguments();

    Object getThis();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
