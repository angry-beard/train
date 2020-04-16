package com.beard.train.prototype;

import java.util.ArrayList;

public class PrototypeTest {

    public static void main(String[] args) {
        ConcretePrototype prototype = new ConcretePrototype();
        prototype.setName("大海");
        prototype.setAge(22);
        System.out.println(prototype);
        ConcretePrototype prototype1 = prototype.clone();
        System.out.println(prototype1);
        ShallowPrototype shallowPrototype = new ShallowPrototype();
        shallowPrototype.setName("小海");
        shallowPrototype.setAge(10);
        shallowPrototype.setHobbies(new ArrayList<>());
        ShallowPrototype shallowCloneType = shallowPrototype.clone();
        shallowCloneType.getHobbies().add("BB");
        System.out.println(shallowPrototype);
        System.out.println(shallowCloneType);
        DeepPrototype deepPrototype = new DeepPrototype();
        deepPrototype.setName("静静");
        deepPrototype.setAge(18);
        deepPrototype.setHobbies(new ArrayList<>());
        DeepPrototype deepCloneType = deepPrototype.clone();
        deepCloneType.getHobbies().add("BB");
        System.out.println(deepPrototype);
        System.out.println(deepCloneType);
    }
}
