package com.beard.train.concurrent.juc.deadlock;

public class DeadLockDemo {

    public static void main(String[] args) {
        DeadLock lockDemo1 = new DeadLock(true);
        DeadLock lockDemo2 = new DeadLock(false);
        Thread thread1 = new Thread(lockDemo1);
        Thread thread2 = new Thread(lockDemo2);
        thread1.start();
        thread2.start();
    }

}

class MyLock {
    public static Object obj1 = new Object();
    public static Object obj2 = new Object();
}

class DeadLock extends Thread {

    private boolean flag;

    public DeadLock(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (MyLock.obj1) {
                    System.out.println(Thread.currentThread().getName() + "---------if获得obj1锁");
                    synchronized (MyLock.obj2) {
                        System.out.println(Thread.currentThread().getName() + "if获得obj2锁");
                    }
                }
            }
        } else {
            synchronized (MyLock.obj2) {
                System.out.println(Thread.currentThread().getName() + "---------else获得obj1锁");
                synchronized (MyLock.obj1) {
                    System.out.println(Thread.currentThread().getName() + "else获得obj2锁");
                }
            }
        }
    }
}
