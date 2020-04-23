package com.beard.train.framework.beans;

import lombok.Data;

@Data
public class BeardBeanWrapper {

    private Object wrapperInstance;
    private Class<?> wrappedClass;

    public BeardBeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrappedClass = instance.getClass();
    }
}
