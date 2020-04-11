package com.beard.train.factory;

/**
 * 抽象工厂 产品族 产品级
 */
public abstract class CourseAbstractFactory {

    public void init() {
        System.out.println("初始化基础数据！");
    }

    public abstract INote createNote();

    public abstract IVideo createVideo();
}
