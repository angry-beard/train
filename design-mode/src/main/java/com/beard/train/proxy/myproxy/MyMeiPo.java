package com.beard.train.proxy.myproxy;

import com.beard.train.proxy.IPerson;

import java.lang.reflect.Method;

public class MyMeiPo implements MyInvocationHandler {

    private IPerson target;


    public IPerson getInstance(IPerson target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        return (IPerson) MyProxy.newProxyInstance(new MyClassLoader(), clazz.getInterfaces(), this);
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
