package com.beard.train.concurrent.juc;

public class VolatileDemo {

    private static long temp;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            temp = 0;
            for (int j = 0; j < 10000; j++) {
                new Thread(() -> {
                    temp = temp + 1;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            System.out.println(temp);
        }
    }
}
