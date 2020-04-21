package com.beard.train.composite;

public abstract class CourseComposite {

    public void addChild(CourseComposite courseComposite) {
        throw new UnsupportedOperationException("不支持添加节点操作");
    }

    public void removeChild(CourseComposite courseComposite) {
        throw new UnsupportedOperationException("不支持删除节点操作");
    }

    public String getName(CourseComposite courseComposite) {
        throw new UnsupportedOperationException("不支持获取名字操作");
    }

    public Integer getPrice(CourseComposite courseComposite) {
        throw new UnsupportedOperationException("不支持获取价格操作");
    }

    public void print() {
        throw new UnsupportedOperationException("不支持打印操作");
    }

}
