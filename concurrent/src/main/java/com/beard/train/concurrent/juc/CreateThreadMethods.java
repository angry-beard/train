package com.beard.train.concurrent.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class CreateThreadMethods {

    public static void main(String[] args) {
        new ExtendThread().start();
        new Thread(new ImplRunnable()).start();
        new Thread(() -> {
            System.out.println("lambda ！");
        }).start();
        new Thread(new FutureTask<>(new ImplCallable())).start();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            System.out.println("ThreadPool");
        });
        executorService.shutdown();
    }

    static class ExtendThread extends Thread {

        @Override
        public void run() {
            System.out.println("ExtendThread.run");
        }
    }

    static class ImplRunnable implements Runnable {

        public void run() {
            System.out.println("ImplRunnable.run");
        }
    }

    static class ImplCallable implements Callable<String> {

        public String call() {
            System.out.println("ImplCallable.call");
            return "success";
        }
    }
}
