package com.beard.train.encrypt;

import com.beard.train.encrypt.utils.AESUtil;
import com.beard.train.encrypt.utils.FileUtil;
import com.beard.train.encrypt.utils.RSAUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * OSS http请求示例
 *
 */
public class HttpApp {
    static final Logger LOGGER = LoggerFactory.getLogger(HttpApp.class);

    //static final String url = "http://localhost:9091/sncfc-oss/rest/HUANBEI";
    static final String url = "http://58.213.77.131:7082/sncfc-oss/rest/HUANBEI";


    /**
     * @param args
     * @throws IOException
     * @throws GeneralSecurityException
     * @author davidwang2006@aliyun.com
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        ClassLoader cl = App.class.getClassLoader();


        //+++++++++++++++++++++++++++
        //加载key

        //请注意这keyA文件是随机生成的，不做为此次测试环境使用,仅用作demo

        String pubKeyA = null;
        String priKeyA = null;

        try (InputStream is0 = cl.getResourceAsStream("a_pri_key.pem");
             InputStream is1 = cl.getResourceAsStream("a_pub_key.pem")) {
            pubKeyA = FileUtil.loadKey(is1);
            priKeyA = FileUtil.loadKey(is0);
        }

        // 业务参数
        Map<String, String> businessParams = new TreeMap<>();
        businessParams.put("partnerId", "HUANBEI");
        businessParams.put("productCode", "PTHB01");
        businessParams.put("orderNo", "BT20190221001");
        businessParams.put("custName", "马六");
        businessParams.put("certType", "0");
        businessParams.put("certNo", "152128199311221810");
        businessParams.put("corporation", "鹅城");
        businessParams.put("address", "河北省邯郸市");
        businessParams.put("mobile", "13900000042");
        businessParams.put("amount", "1000.00");
        businessParams.put("corporation", "鹅城");
        businessParams.put("signTime", "2019-02-28 18:45:12");
        businessParams.put("bankCardNo", "6288888888888888");
        businessParams.put("loanPeriod", "3");
        businessParams.put("custType", "3");
        businessParams.put("paymentType", "2");

        String serviceId = "quotaApply";
        String serialNumber = UUID.randomUUID().toString().replaceAll("\\-", "");//随机生成
        byte[] aesKey = AESUtil.randomKey();
        String encryptKey = Base64.encodeBase64URLSafeString(RSAUtil.encrypt(aesKey, pubKeyA));//使用oss的公钥加密
        //生成待签名的字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : businessParams.entrySet()) {
            sb.append(entry.getValue());
        }
        //生成签名
        byte[] beforeSignArr = sb.toString().getBytes(StandardCharsets.UTF_8);
        String sign = Base64.encodeBase64URLSafeString(RSAUtil.signSha256(beforeSignArr, priKeyA));
        //组装data map
        Map<String, String> dataMap = new HashMap<>(businessParams);
        dataMap.put("sign", sign);
        //加密data map
        String dataMapJson = new Gson().toJson(dataMap);
        byte[] dataRaw = AESUtil.encrypt(dataMapJson.getBytes(StandardCharsets.UTF_8), aesKey);
        String data = Base64.encodeBase64URLSafeString(dataRaw);

        //组装全部参数
        Map<String, String> all = new HashMap<>();
        all.put("serviceId", serviceId);
        all.put("serialNumber", serialNumber);
        all.put("data", data);
        all.put("encryptKey", encryptKey);
        System.out.println(all.toString());
        //接下来发送请求
        //demo使用URLConnection发送请求，实际环境中请根据需要采用此种方式或使用http-client的方式
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try {
            conn.connect();
            byte[] raw = new Gson().toJson(all).getBytes(StandardCharsets.UTF_8);
            conn.getOutputStream().write(raw);
            conn.getOutputStream().flush();
            IOUtils.closeQuietly(conn.getOutputStream());

            //读响应
            byte[] respRaw = IOUtils.toByteArray(conn.getInputStream());
            //转成map
            Map<String, String> resp = new Gson().fromJson(new String(respRaw, StandardCharsets.UTF_8), new TypeToken<Map<String, String>>() {
            }.getType());
            LOGGER.info("got resp {}", resp);
            //先判断外层是否成功
            if (!"0000".equals(resp.get("resultCode"))) {
                LOGGER.error("request failed! {}", resp);
                return;
            }
            encryptKey = resp.get("encryptKey");
            data = resp.get("data");
            //用自己的私钥解密aesKey
            aesKey = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(encryptKey), priKeyA);
            //解密data
            byte[] dataAll = AESUtil.decrypt(Base64.decodeBase64(data), aesKey);
            Map<String, String> dataAllMap = new Gson().fromJson(new String(dataAll, StandardCharsets.UTF_8), new TypeToken<Map<String, String>>() {
            }.getType());
            //获得sign
            sign = dataAllMap.remove("sign");
            //获得原始签名前的字符串
            businessParams = new TreeMap<>(dataAllMap);
            sb = new StringBuilder();
            for (Map.Entry<String, String> entry : businessParams.entrySet()) {
                sb.append(entry.getValue());
            }
            //用OSS公钥验签
            beforeSignArr = sb.toString().getBytes(StandardCharsets.UTF_8);
            boolean ok = RSAUtil.verifySha256(beforeSignArr, pubKeyA, Base64.decodeBase64(sign));
            if (!ok) {
                LOGGER.error("验签失败!");
                return;
            }

            LOGGER.info("得到业务响应参数 {}", businessParams);


        } finally {
            IOUtils.closeQuietly(conn.getInputStream());
        }


    }
}
