package com.beard.train.delegate;

public class EmployeeB implements IEmployee {
    private String goodAt = "设计";

    @Override
    public void doing(String task) {
        System.out.println("我是员工B,我擅长" + goodAt);
    }
}
