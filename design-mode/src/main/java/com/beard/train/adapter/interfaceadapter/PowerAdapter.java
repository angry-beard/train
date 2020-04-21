package com.beard.train.adapter.interfaceadapter;


public class PowerAdapter implements DC {

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

    @Override
    public int output12V() {
        return 0;
    }

    @Override
    public int output24V() {
        return 0;
    }

    @Override
    public int output36V() {
        return 0;
    }
}
