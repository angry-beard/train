package com.beard.train.factory;

public class GoCourseFactory implements ICourseFactoryMethod {
    @Override
    public ICourse create() {
        return new GoCourse();
    }
}
