package com.beard.train.concurrent.juc;

import java.util.concurrent.TimeUnit;

public class WhatThread {

    public static void main(String[] args) {
        new MyThread().start();
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main run");
        }
    }

    private static class MyThread extends Thread {


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("MyThread run");
            }
        }
    }
}
