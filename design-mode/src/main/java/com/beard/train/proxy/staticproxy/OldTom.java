package com.beard.train.proxy.staticproxy;

import com.beard.train.proxy.IPerson;

public class OldTom implements IPerson {

    private Tom tom;

    public OldTom(Tom tom) {
        this.tom = tom;
    }

    @Override
    public void findLove() {
        System.out.println("物色对象");
        tom.findLove();
        System.out.println("开始交往");
    }
}
