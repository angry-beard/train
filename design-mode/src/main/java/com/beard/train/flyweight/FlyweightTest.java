package com.beard.train.flyweight;

public class FlyweightTest {

    public static void main(String[] args) {
        IFlyweight flyweight = FlyweightFactory.getFlyweight("tom");
        flyweight.operation("jack");
    }
}
