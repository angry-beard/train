package com.beard.train.concurrent.juc;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyContainer<T> {

    private LinkedList<T> container = new LinkedList<>();
    private final int MAX_SIZE = 10;
    private Lock lock = new ReentrantLock(true);
    Condition productLock = lock.newCondition();
    Condition consumerLock = lock.newCondition();


    public static void main(String[] args) {
        MyContainer<String> c = new MyContainer<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 200; j++) {
                    System.out.println(c.get());
                }
            }).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    c.put(Thread.currentThread().getName() + " " + j);
                }
            }, "p" + i).start();
        }
    }

    public void put(T t) {
        try {
            lock.lock();
            while (container.size() == MAX_SIZE) {
                try {
                    productLock.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            container.add(t);
            consumerLock.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public T get() {
        T t;
        try {
            lock.lock();
            while (container.size() == 0) {
                try {
                    consumerLock.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t = container.removeFirst();
            productLock.signalAll();
        } finally {
            lock.unlock();
        }
        return t;
    }
}
