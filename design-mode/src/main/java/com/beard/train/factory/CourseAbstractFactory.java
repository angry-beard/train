package com.beard.train.factory;

/**
 * 抽象工厂 产品族 一系列的相关产品整合到一起有关联行 产品等级 相同的产品标准
 */
public abstract class CourseAbstractFactory {

    public void init() {
        System.out.println("初始化基础数据！");
    }

    public abstract INote createNote();

    public abstract IVideo createVideo();
}
