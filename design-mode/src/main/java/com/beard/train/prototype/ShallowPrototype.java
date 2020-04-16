package com.beard.train.prototype;

import java.util.List;

public class ShallowPrototype implements Cloneable {

    private Integer age;
    private String name;
    private List<String> hobbies;

    public ShallowPrototype clone() {
        try {
            return (ShallowPrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "name=" + this.getName() + ";age=" + this.getAge() + ";hobbies=" + hobbies;
    }
}