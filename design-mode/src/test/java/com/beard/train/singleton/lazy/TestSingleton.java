package com.beard.train.singleton.lazy;


public class TestSingleton {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new LazyThread());
        Thread thread2 = new Thread(new LazyThread());
        thread1.start();
        thread2.start();
        System.out.println("END");
    }
}
