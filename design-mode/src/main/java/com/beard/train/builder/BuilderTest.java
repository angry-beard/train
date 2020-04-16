package com.beard.train.builder;

public class BuilderTest {

    public static void main(String[] args) {
        CourseBuilder builder = new CourseBuilder();
        builder.addName("HH")
                .addNote("not")
                .addPpt("ppt")
                .addVideo("video");
        System.out.println(builder.builder());
    }
}
