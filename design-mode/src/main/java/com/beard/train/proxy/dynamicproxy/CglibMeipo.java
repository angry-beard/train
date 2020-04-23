//package com.beard.train.proxy.dynamicproxy;
//
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//
//public class CglibMeipo implements MethodInterceptor {
//
//    public Object getInstance(Class<?> clazz) {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(clazz);
//        enhancer.setCallback(this);
//        return enhancer.create();
//    }
//
//    @Override
//    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//        before();
//        Object result = methodProxy.invokeSuper(o, objects);
//        after();
//        return result;
//    }
//
//    private void after() {
//        System.out.println("同意，开始交往");
//    }
//
//    private void before() {
//        System.out.println("CG媒婆：说出你的需求");
//    }
//
//}
