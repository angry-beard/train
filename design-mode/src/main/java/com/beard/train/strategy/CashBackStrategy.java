package com.beard.train.strategy;

public class CashBackStrategy implements IPromotionStrategy {

    @Override
    public void doPromotion() {
        System.out.println("返现");
    }
}
