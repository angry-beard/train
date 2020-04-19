package com.beard.train.proxy.staticproxy;

import com.beard.train.proxy.IPerson;

public class Tom implements IPerson {

    @Override
    public void findLove() {
        System.out.println("tom要求：肤白貌美大长腿");
    }

    public void work() {
        System.out.println("hard work");
    }
}
