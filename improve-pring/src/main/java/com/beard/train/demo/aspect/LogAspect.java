package com.beard.train.demo.aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before() {
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!");
    }

    //在调用一个方法之后，执行after方法
    public void after() {
        log.info("Invoker After Method!!!");
    }

    public void afterThrowing() {

        log.info("出现异常");
    }
}
