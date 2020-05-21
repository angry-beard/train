package com.beard.train.concurrent.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CasDemo {

    AtomicInteger count = new AtomicInteger();
    Integer bak = 0;

    public static void main(String[] args) {
        CasDemo cas = new CasDemo();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(cas::m, "Thread-" + i));
        }
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(cas.count);
        System.out.println(cas.bak);
    }

    synchronized void m() {
        for (int i = 0; i < 100; i++) {
            count.incrementAndGet();
            bak += 1;
        }
    }
}
