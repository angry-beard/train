package com.beard.train.demo.controller;

import com.beard.train.demo.service.IDemoService;
import com.beard.train.framework.annotation.BeardAutowired;
import com.beard.train.framework.annotation.BeardController;
import com.beard.train.framework.annotation.BeardRequestMapping;
import com.beard.train.framework.annotation.BeardRequestParam;
import com.beard.train.framework.webmvc.servlet.BeardModelAndView;

import java.util.HashMap;
import java.util.Map;

@BeardController
@BeardRequestMapping("/")
public class CommController {

    @BeardAutowired
    private IDemoService demoService;

    @BeardRequestMapping("/first.html")
    public BeardModelAndView query(@BeardRequestParam("teacher") String teacher) {
        String result = demoService.out(teacher);
        Map<String, Object> model = new HashMap<>();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "4232324f");
        return new BeardModelAndView("first.html", model);
    }
}
