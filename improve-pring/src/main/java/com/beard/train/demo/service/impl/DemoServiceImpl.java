package com.beard.train.demo.service.impl;

import com.beard.train.demo.service.IDemoService;
import com.beard.train.framework.annotation.BeardService;

@BeardService
public class DemoServiceImpl implements IDemoService {

    public String out(String name) {
        return "My name is " + name;
    }

}
