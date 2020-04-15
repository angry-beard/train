package com.beard.train.singleton.lazy;

public class LazyThread implements Runnable {

    public void run() {
        LazySimpleSingleton singleton = LazySimpleSingleton.getInstance();
        System.out.println(Thread.currentThread().getName() + ":" + singleton);
    }
}
