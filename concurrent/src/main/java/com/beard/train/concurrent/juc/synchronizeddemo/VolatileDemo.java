package com.beard.train.concurrent.juc.synchronizeddemo;

public class VolatileDemo {

    private static boolean flag = false;

    public static void main(String[] args) {
        for (int j = 0; j < 10000; j++) {
            new Thread(() -> {
                int i = 0;
                while (!flag) {
                    i++;
                }
                System.out.println(i);
            }
            ).start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                flag = true;
            }).start();
        }
    }
}
