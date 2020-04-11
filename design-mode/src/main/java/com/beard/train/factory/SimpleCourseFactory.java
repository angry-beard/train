package com.beard.train.factory;

import java.util.Objects;

/**
 * 简单工厂：产品的工厂
 * 适合创建对象较少的场景
 * 不易于扩展过于复杂的产品结构
 */
public class SimpleCourseFactory {

    public ICourse create(Class<? extends ICourse> clazz) {
        if (Objects.nonNull(clazz)) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
