package com.beard.train.encrypt;

import com.beard.train.encrypt.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class App {


    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Logger logger = LoggerFactory.getLogger(App.class);
        ClassLoader cl = App.class.getClassLoader();


        //+++++++++++++++++++++++++++
        //加载key

        //请注意这四个key文件是随机生成的，不做为此次测试环境使用,仅用作demo

        String pubKeyA = null;
        String priKeyA = null;
        String pubKeyB = null;
        String priKeyB = null;
        File ba = new File("a_pri_key.pem");
        System.out.println(ba.getName());
        System.out.println();
        try (InputStream is0 = cl.getResourceAsStream("a_pri_key.pem");
             InputStream is1 = cl.getResourceAsStream("a_pub_key.pem");
             InputStream is2 = cl.getResourceAsStream("b_pri_key.pem");
             InputStream is3 = cl.getResourceAsStream("b_pub_key.pem")) {

            pubKeyA = FileUtil.loadKey(is1);
            priKeyA = FileUtil.loadKey(is0);
            pubKeyB = FileUtil.loadKey(is3);
            priKeyB = FileUtil.loadKey(is2);
        }


        //假设文件由A提供给B
        //指定文件
        File txt = new File("/Users/angry_beary/Desktop/temp/tmp.txt");
        File cry = new File("/Users/angry_beary/Desktop/temp/tmp.cry");
        File txt2 = new File("/Users/angry_beary/Desktop/temp/tmp-decrypted.txt");

        //由A方加密
        FileUtil.encrypt(txt, cry, priKeyA, pubKeyB);
        logger.info("A加密成功");

        //B方解密
        FileUtil.decrypt(cry, txt2, priKeyB, pubKeyA);
        logger.info("B解密成功");


    }
}
