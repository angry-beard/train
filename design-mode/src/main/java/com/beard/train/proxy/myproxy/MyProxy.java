package com.beard.train.proxy.myproxy;

public class MyProxy {

    public static Object newProxyInstance(MyClassLoader loader, Class<?>[] interfaces, MyInvocationHandler h) throws IllegalArgumentException {
        //1、动态生成源码.java文件
        String src = generateSrc(interfaces);
        //2、java文件输出到磁盘，保存为文件$Proxy0.java
        //3、把.java文件编译成$Proxy0.class文件
        //4、把生成的.class文件加载到JVM中
        //5、返回新的代理对象
        return null;
    }

    private static String generateSrc(Class<?>[] interfaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        return sb.toString();
    }
}
