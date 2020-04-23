package com.beard.train.demo.controller;

import com.beard.train.framework.annotation.BeardAutowired;
import com.beard.train.framework.annotation.BeardController;
import com.beard.train.framework.annotation.BeardRequestMapping;
import com.beard.train.framework.annotation.BeardRequestParam;
import com.beard.train.demo.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@BeardController
public class DemoController {

    @BeardAutowired
    private IDemoService demoService;

    @BeardRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @BeardRequestParam("name") String name) {
        String result = demoService.out(name);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeardRequestMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response, @BeardRequestParam("a") Integer a, @BeardRequestParam("b") Integer b) {
        try {
            response.getWriter().write(a + "+" + b + "=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
