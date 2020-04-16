package com.beard.train.prototype;

public class ConcretePrototype implements IPrototype {

    private Integer age;
    private String name;

    @Override
    public ConcretePrototype clone() {
        ConcretePrototype prototype = new ConcretePrototype();
        prototype.setAge(this.age);
        prototype.setName(this.name);
        return prototype;
    }

    public Integer getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name=" + this.getName() + ";age=" + this.getAge();
    }
}
