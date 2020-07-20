package com.beard.train.encrypt.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author davidwang2006@aliyun.com
 * @date 2018/12/13 15:59
 */
public abstract class FileUtil {
    private FileUtil() {
    }


    /**
     * 加密文件
     *
     * @param source
     * @param target
     * @param myPriKey     已方私钥
     * @param remotePubKey 对方的公钥
     * @throws IOException
     */
    public static void encrypt(File source, File target, String myPriKey, String remotePubKey) throws IOException, GeneralSecurityException {

        //得到原始文件内容
        byte[] sourceArr;
        try (InputStream is = new FileInputStream(source)) {
            sourceArr = IOUtils.toByteArray(is);
        }


        //step 1. 先压缩
        ByteArrayOutputStream zipFilContainer = new ByteArrayOutputStream((int) source.length());
        try (ZipOutputStream zos = new ZipOutputStream(zipFilContainer);
             InputStream is = new FileInputStream(source)) {
            zos.putNextEntry(new ZipEntry(source.getName()));
            zos.write(sourceArr);
        }

        //得到压缩后的文件内容
        byte[] zipArr = zipFilContainer.toByteArray();

        //step 2. 进行AES加密
        //生成随机AES密钥
        byte[] aesKey = AESUtil.randomKey();

        //得到文档中描述的 file.tmp
        byte[] fileTmpArr = AESUtil.encrypt(zipArr, aesKey);


        //step 3. 将AES密钥使用  对方公钥进行加密,得到文档中描述的bytesA
        byte[] bytesA = RSAUtil.encrypt(aesKey, remotePubKey);

        //step 4. 将原始文件内容进行签名,使用已方私钥,得到文档中描述的bytesB
        byte[] bytesB = RSAUtil.signSha256(sourceArr, myPriKey);


        //step LAST, 进行拼接
        try (OutputStream fos = new FileOutputStream(target);
             DataOutputStream dos = new DataOutputStream(fos);) {
            //bytesA的长度
            dos.writeShort(bytesA.length);
            //bytesA
            dos.write(bytesA);
            //bytesB的长度
            dos.writeShort(bytesB.length);
            //bytesB
            dos.write(bytesB);
            //file.tmp
            dos.write(fileTmpArr);
        }

    }

    /**
     * 解密文件
     *
     * @param source
     * @param target
     * @param myPriKey
     * @param remotePubKey
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void decrypt(File source, File target, String myPriKey, String remotePubKey) throws IOException, GeneralSecurityException {
        //encrypt的逆操作

        try (InputStream is = new FileInputStream(source);
             DataInputStream dis = new DataInputStream(is)) {
            //读取bytesA的长度
            int len = dis.readShort();
            byte[] bytesA = new byte[len];
            //得到bytesA
            dis.readFully(bytesA);
            //读取bytesB的长度
            len = dis.readShort();
            byte[] bytesB = new byte[len];
            //得到bytesB
            dis.readFully(bytesB);

            //得到剩余部分, 即加密时的file.tmp
            byte[] fileTmpArr = IOUtils.toByteArray(dis);

            //解密得到aesKey
            byte[] aesKey = RSAUtil.decryptByPrivateKey(bytesA, myPriKey);
            //对fileTmpArr进行解密
            byte[] zipArr = AESUtil.decrypt(fileTmpArr, aesKey);

            //对zipArr进行解压
            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
            try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipArr))) {
                zis.getNextEntry();
                IOUtils.copy(zis, bos);
            }
            //得到明文文件内容
            byte[] txtArr = bos.toByteArray();

            //下面进行验签
            if (!RSAUtil.verifySha256(txtArr, remotePubKey, bytesB)) {
                throw new GeneralSecurityException("验签失败");
            }

            try (FileOutputStream fos = new FileOutputStream(target)) {
                fos.write(txtArr);
            }

        }


    }

    /**
     * 加载key文件内容
     *
     * @param is
     * @return
     */
    public static String loadKey(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) continue;
                sb.append(line.trim());
            }

        }

        return sb.toString();

    }

}
