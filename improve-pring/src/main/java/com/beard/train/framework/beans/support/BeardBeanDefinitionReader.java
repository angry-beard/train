package com.beard.train.framework.beans.support;

import com.beard.train.framework.beans.config.BeardBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class BeardBeanDefinitionReader {

    private Properties config = new Properties();
    private List<String> registryBeanClasses = new ArrayList<>();

    public BeardBeanDefinitionReader(String... locations) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replaceAll("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(is)) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
        //扫描配置文件中相关的类
        doScanner(config.getProperty("scanPackage"));

    }

    public Properties getConfig() {
        return this.config;
    }

    public List<BeardBeanDefinition> loadBeanDefinition() {
        List<BeardBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                //保存类对应的ClassName(全类名) 还有beanName
                result.add(BeardBeanDefinition.builder()
                        .factoryBeanName(toLowerFirstCase(beanClass.getSimpleName()))
                        .beanClassName(beanClass.getName())
                        .lazyInit(false)
                        .build());
                for (Class<?> i : beanClass.getInterfaces()) {
                    result.add(BeardBeanDefinition.builder()
                            .factoryBeanName(toLowerFirstCase(i.getSimpleName()))
                            .beanClassName(beanClass.getName())
                            .lazyInit(false)
                            .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        assert url != null;
        File classPath = new File(url.getFile());
        for (File file : Objects.requireNonNull(classPath.listFiles())) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String clazzName = (scanPackage + "." + file.getName().replaceAll(".class", ""));
                registryBeanClasses.add(clazzName);
            }
        }
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
