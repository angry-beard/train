package com.beard.train.bridge;

public abstract class AbstractCourse implements ICourse {

    private IVideo video;
    private INote note;

    public void setNote(INote note) {
        this.note = note;
    }

    public void setVideo(IVideo video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "AbstractCourse{" +
                "video=" + video +
                ", note=" + note +
                '}';
    }
}
