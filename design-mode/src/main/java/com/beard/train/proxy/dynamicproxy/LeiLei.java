package com.beard.train.proxy.dynamicproxy;

import com.beard.train.proxy.IPerson;

public class LeiLei implements IPerson {

    @Override
    public void findLove() {
        System.out.println("LeiLei: 可爱易推倒");
    }
}
