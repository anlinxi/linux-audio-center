package com.faker.audioStation.util;


import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;

/**
 * @program: xjwlcs 新疆移动
 * @description: QEncodeUtil AES加密解密统一工具类
 * @author: 淡梦如烟
 * @create: 2019-09-06 11:38
 */
public class QEncodeUtil {

    /**
     * 编码格式 默认UTF-8
     */
    private static String charsetName = "UTF-8";

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return Base64Encoder.encode(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return isNullOrEmpty(base64Code) ? null : Base64Decoder.decode(base64Code);
    }

    /**
     * 获取byte[]的md5值
     *
     * @param bytes byte[]
     * @return md5
     * @throws Exception
     */
    public static byte[] md5(byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);

        return md.digest();
    }

    /**
     * 获取字符串md5值
     *
     * @param msg md5
     * @throws Exception 异常
     */
    public static byte[] md5(String msg) throws Exception {
        return isNullOrEmpty(msg) ? null : md5(msg.getBytes());
    }

    /**
     * 结合base64实现md5加密
     *
     * @param msg 待加密字符串
     * @return 获取md5后转为base64
     * @throws Exception 异常
     */
    public static String md5Encrypt(String msg) throws Exception {
        return isNullOrEmpty(msg) ? null : base64Encode(md5(msg));
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception 异常
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        Key key1 = new SecretKeySpec(encryptKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key1);
        return cipher.doFinal(content.getBytes(charsetName));
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey)).replaceAll("\r\n", "").replaceAll("\n", "");
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception 异常
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes, charsetName);
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception 异常
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        if (null != encryptStr) {
            encryptStr = encryptStr.replaceAll(" ", "+");
        }
        return isNullOrEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * 简化版的isNullOrEmpty
     * 判断字符串是否为空
     *
     * @param text 字符串
     * @return 是否为空
     */
    public static Boolean isNullOrEmpty(String text) {
        if (null == text) {
            return true;
        } else if ("".equals(text.trim())) {
            return true;
        } else {
            return false;
        }
    }
}