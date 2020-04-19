package com.beard.train.proxy;

import com.beard.train.proxy.dynamicproxy.CglibMeipo;
import com.beard.train.proxy.dynamicproxy.JdkMeiPo;
import com.beard.train.proxy.dynamicproxy.LeiLei;
import com.beard.train.proxy.staticproxy.OldTom;
import com.beard.train.proxy.staticproxy.Tom;

public class ProxyTest {

    public static void main(String[] args) {
        //静态代理
        OldTom oldTom = new OldTom(new Tom());
        oldTom.findLove();
        System.out.println("《《《《《《《《《《《《《《《《《《《《《《");
        //jkd动态代理
        JdkMeiPo meiPo = new JdkMeiPo();
        IPerson tomProxy = meiPo.getInstance(new Tom());
        meiPo = new JdkMeiPo();
        IPerson leiLeiProxy = meiPo.getInstance(new LeiLei());
        tomProxy.findLove();
        leiLeiProxy.findLove();
        System.out.println("《《《《《《《《《《《《《《《《《《《《《《");
        //cglib 动态代理
        CglibMeipo cglibMeipo = new CglibMeipo();
        Tom cgTom = (Tom) cglibMeipo.getInstance(Tom.class);
        cgTom.findLove();


    }
}
