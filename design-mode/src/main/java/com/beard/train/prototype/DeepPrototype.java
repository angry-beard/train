package com.beard.train.prototype;

import java.io.*;
import java.util.List;

public class DeepPrototype implements Serializable {

    private Integer age;
    private String name;
    private List<String> hobbies;

    public DeepPrototype clone() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (DeepPrototype) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
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