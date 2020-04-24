package com.beard.train.framework.context;

import com.beard.train.framework.annotation.BeardAutowired;
import com.beard.train.framework.annotation.BeardController;
import com.beard.train.framework.annotation.BeardService;
import com.beard.train.framework.aop.JdkDynamicAopProxy;
import com.beard.train.framework.aop.config.BeardAopConfig;
import com.beard.train.framework.aop.support.BeardAdvisedSupport;
import com.beard.train.framework.beans.BeardBeanWrapper;
import com.beard.train.framework.beans.config.BeardBeanDefinition;
import com.beard.train.framework.beans.support.BeardBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 完成Bean的创建和DI
 */
public class BeardApplicationContext {


    private BeardBeanDefinitionReader reader;
    private Map<String, BeardBeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, BeardBeanWrapper> factoryBeanInstanceCache = new HashMap<>();
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    public BeardApplicationContext(String... configLocations) {
        //1、加载配置文件
        reader = new BeardBeanDefinitionReader(configLocations);
        //2、解析配置文件，封装成BeanDefinition
        List<BeardBeanDefinition> beanDefinitions = reader.loadBeanDefinition();
        //3、把BeanDefinition缓存起来
        try {
            doRegisterBeanDefinition(beanDefinitions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        doAutowired();
    }

    private void doAutowired() {
        for (Map.Entry<String, BeardBeanDefinition> beardBeanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beardBeanDefinitionEntry.getKey();
            getBean(beanName);
        }
    }


    private void doRegisterBeanDefinition(List<BeardBeanDefinition> beanDefinitions) throws Exception {
        for (BeardBeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists!");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    public Object getBean(String beanName) {
        //1、先拿到beanDefinition配置信息
        BeardBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        //2、反射实例化
        Object instance = instantiateBean(beanName, beanDefinition);
        //3、封装成BeanWrapper
        BeardBeanWrapper beanWrapper = new BeardBeanWrapper(instance);
        //4、保存到IoC容器
        factoryBeanInstanceCache.put(beanName, beanWrapper);
        //5、执行依赖注入
        populateBean(beanName, beanDefinition, beanWrapper);
        return beanWrapper.getWrapperInstance();
    }

    private void populateBean(String beanName, BeardBeanDefinition beanDefinition, BeardBeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrappedClass();
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
        String className = beanDefinition.getBeanClassName();
        Class<?> clazz;
        Object instance = null;
        try {
            if (this.factoryBeanObjectCache.containsKey(beanName)) {
                instance = this.factoryBeanObjectCache.get(beanName);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
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
        return beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.entrySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
