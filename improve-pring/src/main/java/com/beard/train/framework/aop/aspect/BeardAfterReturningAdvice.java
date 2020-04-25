package com.beard.train.framework.aop.aspect;

import com.beard.train.framework.aop.BeardJoinPoint;
import com.beard.train.framework.aop.intercept.BeardMethodInterceptor;
import com.beard.train.framework.aop.intercept.BeardMethodInvocation;

import java.lang.reflect.Method;

public class BeardAfterReturningAdvice extends BeardAbstractAspectJAdvice implements BeardAdvice, BeardMethodInterceptor {

    private BeardJoinPoint joinPoint;

    public BeardAfterReturningAdvice(Object newInstance, Method method) {
        super(newInstance, method);
    }

    @Override
    public Object invoke(BeardMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        invokeAdviceMethod(joinPoint, retVal, null);
    }
}
