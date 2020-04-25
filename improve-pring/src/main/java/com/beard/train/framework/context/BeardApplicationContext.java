package com.beard.train.framework.context;

import com.beard.train.framework.annotation.BeardAutowired;
import com.beard.train.framework.annotation.BeardController;
import com.beard.train.framework.annotation.BeardService;
import com.beard.train.framework.aop.JdkDynamicAopProxy;
import com.beard.train.framework.aop.config.BeardAopConfig;
import com.beard.train.framework.aop.support.BeardAdvisedSupport;
import com.beard.train.framework.beans.BeardBeanWrapper;
import com.beard.train.framework.beans.config.BeardBeanDefinition;
import com.beard.train.framework.beans.config.BeardBeanPostProcessor;
import com.beard.train.framework.beans.factory.BeardFactoryBean;
import com.beard.train.framework.beans.support.BeardBeanDefinitionReader;
import com.beard.train.framework.context.factory.BeardDefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 完成Bean的创建和DI
 */
public class BeardApplicationContext extends BeardDefaultListableBeanFactory implements BeardFactoryBean {


    private BeardBeanDefinitionReader reader;
    private String[] configLocation;
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();
    private Map<String, BeardBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public BeardApplicationContext(String... configLocations) {
        this.configLocation = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doAutowired() {
        for (Map.Entry<String, BeardBeanDefinition> beardBeanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beardBeanDefinitionEntry.getKey();
            if (!beardBeanDefinitionEntry.getValue().getLazyInit()) {
                getBean(beanName);
            }
        }
    }


    private void doRegisterBeanDefinition(List<BeardBeanDefinition> beanDefinitions) throws Exception {
        for (BeardBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    public Object getBean(String beanName) {
        //1、先拿到beanDefinition配置信息
        BeardBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        BeardBeanPostProcessor postProcessor = new BeardBeanPostProcessor();
        //2、反射实例化
        Object instance = instantiateBean(beanName, beanDefinition);
        if (Objects.isNull(instance)) {
            return null;
        }
        postProcessor.postProcessBeforeInitialization(instance, beanName);
        //3、封装成BeanWrapper
        BeardBeanWrapper beanWrapper = new BeardBeanWrapper(instance);
        //4、保存到IoC容器
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        postProcessor.postProcessAfterInitialization(instance, beanName);
        //5、执行依赖注入
        populateBean(beanName, instance);
        return this.factoryBeanInstanceCache.get(beanName).getWrapperInstance();
    }

    private void populateBean(String beanName, Object instance) {
        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(BeardController.class) || clazz.isAnnotationPresent(BeardService.class))) {
            return;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(BeardAutowired.class)) {
                continue;
            }
            BeardAutowired beardAutowired = field.getAnnotation(BeardAutowired.class);
            String autowiredBeanName = beardAutowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
                autowiredBeanName = toLowerFirstCase(autowiredBeanName.substring(autowiredBeanName.lastIndexOf(".") + 1));
            }
            field.setAccessible(true);
            try {
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private Object instantiateBean(String beanName, BeardBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        Class<?> clazz;
        try {
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                clazz = Class.forName(className);
                instance = clazz.newInstance();

                //如果满足条件，就直接返回Proxy对象
                BeardAdvisedSupport config = instantionAopConfig(beanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(instance);
                if (config.pointCutMatch()) {
                    instance = new JdkDynamicAopProxy(config).getProxy();
                }

                this.factoryBeanObjectCache.put(beanName, instance);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return instance;
    }

    private BeardAdvisedSupport instantionAopConfig(BeardBeanDefinition beanDefinition) {
        BeardAopConfig aopConfig = new BeardAopConfig();
        aopConfig.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        aopConfig.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        aopConfig.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        aopConfig.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        aopConfig.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new BeardAdvisedSupport(aopConfig);
    }

    public Object getBean(Class beanClass) {
        return getBean(beanClass.getName());
    }

    public int getBeanDefinitionCount() {
        return super.beanDefinitionMap.size();
    }

    public Set<String> getBeanDefinitionNames() {
        return super.beanDefinitionMap.keySet();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

    public void refresh() throws Exception {
        reader = new BeardBeanDefinitionReader(this.configLocation);
        List<BeardBeanDefinition> beanDefinitions = reader.loadBeanDefinition();
        doRegisterBeanDefinition(beanDefinitions);
        doAutowired();
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
