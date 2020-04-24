package com.beard.train.framework.beans.config;

public class BeardBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object instance, String beanName) {
        return instance;
    }

    public Object postProcessAfterInitialization(Object instance, String beanName) {
        return instance;
    }
}
