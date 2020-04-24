package com.beard.train.framework.context.factory;

import com.beard.train.framework.beans.config.BeardBeanDefinition;
import com.beard.train.framework.context.BeardAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeardDefaultListableBeanFactory extends BeardAbstractApplicationContext {

    protected final Map<String, BeardBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
}
