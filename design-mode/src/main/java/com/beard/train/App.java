package com.beard.train;

import com.beard.train.factory.*;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        //简单工厂
        ICourse course = new SimpleCourseFactory().create(JavaCourse.class);
        course.record();
        //工厂方法
        ICourseFactoryMethod factoryMethod = new GoCourseFactory();
        ICourse course1 = factoryMethod.create();
        course1.record();
        //抽象工厂
        CourseAbstractFactory abstractFactory = new JavaCourseFactory();
        abstractFactory.createNote().note();
        abstractFactory.createVideo().video();
    }
}
