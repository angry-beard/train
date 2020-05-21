package com.beard.train.concurrent.juc.synchronizeddemo;

public class SynchronizedThis {

    private int count = 10;

    public void m() {
        synchronized (this) {
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }
}
