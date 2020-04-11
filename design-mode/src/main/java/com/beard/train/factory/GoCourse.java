package com.beard.train.factory;

public class GoCourse implements ICourse {

    @Override
    public void record() {
        System.out.println("go课程");
    }
}
