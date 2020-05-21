package com.beard.train.concurrent.juc;

import java.util.concurrent.TimeUnit;

public class SomeThreadMethod {

    public static void main(String[] args) {
//        testSleep();
//        testYield();
        testJoin();
    }

    private static void testJoin() {
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("bThread:Loop i = " + i);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread threadA = new Thread(() -> {
            try {
                threadB.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("aThread:Loop i = " + i);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadB.start();
        threadA.start();
    }

    private static void testYield() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("In yield:Loop i = " + i);
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("Out yield:Loop i = " + i);
                if (i % 10 == 0) {
                    Thread.yield();
                }
            }
        }).start();
    }

    private static void testSleep() {
        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("sleepMethod:Loop i = " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
