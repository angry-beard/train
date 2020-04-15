package com.beard.train.singleton.lazy;

import java.util.Objects;

public class LazySimpleSingleton {

    private volatile static LazySimpleSingleton singleton;

    private LazySimpleSingleton() {
    }

    public static LazySimpleSingleton getInstance() {
        if (Objects.isNull(singleton)) {
            synchronized (LazySimpleSingleton.class) {
                if (Objects.isNull(singleton)) {
                    return new LazySimpleSingleton();
                }
            }
        }
        return singleton;
    }
}
