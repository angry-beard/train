package com.beard.train.demo.controller;

import com.beard.train.demo.service.IDemoService;
import com.beard.train.demo.service.IModifyService;
import com.beard.train.framework.annotation.BeardAutowired;
import com.beard.train.framework.annotation.BeardController;
import com.beard.train.framework.annotation.BeardRequestMapping;
import com.beard.train.framework.annotation.BeardRequestParam;
import com.beard.train.framework.webmvc.servlet.BeardModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@BeardController
@BeardRequestMapping("/web")
public class DemoController {

    @BeardAutowired
    private IDemoService demoService;
    @BeardAutowired
    private IModifyService modifyService;

    @BeardRequestMapping("/query.json")
    public BeardModelAndView query(HttpServletRequest request, HttpServletResponse response, @BeardRequestParam("name") String name) {
        String result = demoService.out(name);
        return out(response, result);
    }

    @BeardRequestMapping("/add*.json")
    public BeardModelAndView add(HttpServletRequest request, HttpServletResponse response, @BeardRequestParam("name") String name, @BeardRequestParam("addr") String addr) {
        return out(response, modifyService.add(name, addr));
    }

    @BeardRequestMapping("/remove.json")
    public BeardModelAndView remove(HttpServletRequest request, HttpServletResponse response, @BeardRequestParam("id") Integer id) {
        return out(response, modifyService.remove(id));
    }

    @BeardRequestMapping("/edit.json")
    public BeardModelAndView edit(HttpServletRequest request, HttpServletResponse response, @BeardRequestParam("name") String name, @BeardRequestParam("id") Integer id) {
        return out(response, modifyService.edit(id, name));
    }

    private BeardModelAndView out(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
