package com.beard.train.factory;

public class JavaCourseFactory extends CourseAbstractFactory implements ICourseFactoryMethod {
    @Override
    public ICourse create() {
        return new JavaCourse();
    }


    @Override
    public INote createNote() {
        super.init();
        return new JavaNote();
    }

    @Override
    public IVideo createVideo() {
        super.init();
        return new JavaVideo();
    }
}
