package com.beard.train.framework.beans.factory;

public interface BeardFactoryBean {

    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
