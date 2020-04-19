package com.beard.train.proxy.dynamicproxy;

import com.beard.train.proxy.IPerson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkMeiPo implements InvocationHandler {

    private IPerson target;


    public IPerson getInstance(IPerson target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        return (IPerson) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    private void after() {
        System.out.println("开始交往");
    }

    private void before() {
        System.out.println("媒婆：说出你的需求");
    }
}
