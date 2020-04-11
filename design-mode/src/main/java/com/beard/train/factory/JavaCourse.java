package com.beard.train.factory;

public class JavaCourse implements ICourse {

    @Override
    public void record() {
        System.out.println("JAVA课程");
    }
}
