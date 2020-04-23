package com.beard.train.strategy;

public class CouponStrategy implements IPromotionStrategy {

    @Override
    public void doPromotion() {
        System.out.println("实现优惠劵");
    }
}
