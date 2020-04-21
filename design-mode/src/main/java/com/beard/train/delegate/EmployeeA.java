package com.beard.train.delegate;

public class EmployeeA implements IEmployee {

    private String goodAt = "编程";

    @Override
    public void doing(String task) {
        System.out.println("我是员工A,我擅长" + goodAt);
    }
}
