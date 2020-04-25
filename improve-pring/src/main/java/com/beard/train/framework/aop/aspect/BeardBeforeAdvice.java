package com.beard.train.framework.aop.aspect;

import com.beard.train.framework.aop.BeardJoinPoint;
import com.beard.train.framework.aop.intercept.BeardMethodInterceptor;
import com.beard.train.framework.aop.intercept.BeardMethodInvocation;

import java.lang.reflect.Method;

public class BeardBeforeAdvice extends BeardAbstractAspectJAdvice implements BeardAdvice, BeardMethodInterceptor {

    private BeardJoinPoint joinPoint;

    public BeardBeforeAdvice(Object newInstance, Method method) {
        super(newInstance, method);
    }

    @Override
    public Object invoke(BeardMethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        this.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }

    private void before(Method method, Object[] arguments, Object aThis) throws Throwable {
        invokeAdviceMethod(this.joinPoint, null, null);
    }
}
