package com.beard.train.flyweight;

public class ConcreteFlyweight implements IFlyweight {

    private String intrinsicStatus;

    public ConcreteFlyweight(String intrinsicStatus) {
        this.intrinsicStatus = intrinsicStatus;
    }

    @Override
    public void operation(String extrinsicState) {
        System.out.println("Object address:" + System.identityHashCode(this));
        System.out.println("IntrinsicState:" + this.intrinsicStatus);
        System.out.println("ExtrinsicState:" + extrinsicState);
    }
}
