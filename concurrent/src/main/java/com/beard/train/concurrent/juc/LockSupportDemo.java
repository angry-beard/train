package com.beard.train.concurrent.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    private List<Integer> list = new ArrayList<>();
    private final Object lock = new Object();

    public static void main(String[] args) {
        LockSupportDemo lockSupportDemo = new LockSupportDemo();
        new Thread(() -> {
            synchronized (lockSupportDemo.lock) {
                if (lockSupportDemo.size() != 5) {//LockSupport.park(); 可去掉synchronized
                    try {
                        lockSupportDemo.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("监控线程结束");
                lockSupportDemo.lock.notify();//LockSupport.unpark(threadFiled);
            }
        }).start();
        new Thread(() -> {
            synchronized (lockSupportDemo.lock) {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        lockSupportDemo.lock.notify();
                        try {
                            lockSupportDemo.lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    lockSupportDemo.add(i);
                }
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(lockSupportDemo.list);
    }

    public void add(Integer value) {
        list.add(value);
    }

    public Integer size() {
        return list.size();
    }
}
