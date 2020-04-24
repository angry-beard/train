package com.beard.train.demo.service.impl;

import com.beard.train.demo.service.IDemoService;
import com.beard.train.framework.annotation.BeardService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@BeardService
@Slf4j
public class DemoServiceImpl implements IDemoService {

    public String out(String name) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        log.info("业务方法打印的：" + json);
        return json;
    }

}
