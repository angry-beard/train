package com.beard.train.adapter.objectadapter;

import com.beard.train.adapter.classadapter.DC5;

public class PowerAdapter implements DC5 {

    private AC220 adaptee;

    public PowerAdapter(AC220 adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public int output5V() {
        int adapterInput = adaptee.outputAC220V();
        int adapterOutput = adapterInput / 44;
        System.out.println("使用Adapter输入AC " + adapterInput + "V,输出DC " + adapterOutput + "V");
        return adapterOutput;
    }
}
