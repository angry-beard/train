package com.beard.train.demo.service.impl;

import com.beard.train.demo.service.IModifyService;
import com.beard.train.framework.annotation.BeardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BeardService
public class ModifyServiceImpl implements IModifyService {

    @Override
    public String add(String name, String addr) {
        return "modifyService add name=" + name + ",addr" + addr;
    }

    @Override
    public String edit(Integer id, String name) {
        return "modifyService edit name=" + name + ",id" + id;
    }

    @Override
    public String remove(Integer id) {
        return "modifyService remove id=" + id;
    }
}
