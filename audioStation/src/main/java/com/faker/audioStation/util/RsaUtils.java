package com.faker.audioStation.util;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Rsa非对称加解密工具类
 */
public class RsaUtils {

    /**
     * 类型
     */
    public static final String ENCRYPT_TYPE = "RSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWxvY1UucbZYv0svpDYIeBgbO7rRHhIr1KknwP\\r\\nOnSMEODbvBgafcQ2zCMg8NmD//wp3XomfWAIlDx1f4poOPL643pQYeUqCfTZnnZofyrvbGx52MAj\\r\\nlC3hlK1tKYE7/tLtZV49h5DK4X4WT9C/Rqbb71PjvkJTVSyXIRu//z9+dwIDAQAB";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJbG9jVS5xtli/Sy+kNgh4GBs7ut\\r\\nEeEivUqSfA86dIwQ4Nu8GBp9xDbMIyDw2YP//CndeiZ9YAiUPHV/img48vrjelBh5SoJ9Nmedmh/\\r\\nKu9sbHnYwCOULeGUrW0pgTv+0u1lXj2HkMrhfhZP0L9GptvvU+O+QlNVLJchG7//P353AgMBAAEC\\r\\ngYBjCussd/rL6laXNQJkRAJ/Nd4EyFlYVGOXmsXXkwSABY5PiS0kKb08abghqVSY+wx/y7azleQB\\r\\nfq6AWHlooRAqbvZPC23Nc0rp6yTWeggpLFjkqt7YRvIqtXk7wtkW7TmtZvM+Wz8XwH7f2sK2CO8c\\r\\naiQ5moOBLFGDnL5IWiQZgQJBANhdXeZetFJWbzlHci/w0UmZ3GDR9gGaxgzigLbFPxgNyo94QV8E\\r\\nmU5XvFiHHutcX3HXRsjcj7fN+iyZmO1huhcCQQCyZc343LEa6ZvYu5bHTu9552kCW41uvOn8WIo1\\r\\n1htJXFR2P/X1iovk0Pr3Nn9a8HJ3/QNXoNnkQrNG6qm/yfqhAkEAotDXyXS400ER35OhHOxmQbGT\\r\\nwhQdwKVcpfOIkoEp8ormREJlRwBp8Sdap2++17QiasNqE9rOF63btdY7215wLwJAeKmBCg0DQSe2\\r\\nwH4I+p+6PCK4FN8lpzvhq0ubl9RMOm9XtW+Hhd+CxW5QAPt8Yl71nZClPRTPq6MtrQ61Z8JOYQJA\\r\\na1ucELG3d05dyZbzg/QHvG0XioLwmiSkIjrNrDojiiQp/GPTPO/67b8SD4EmkpSx3k6gC0gyDAXi\\r\\njMYqx3ROAw==";


    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String decryptPri(String content, String privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param content    要加密的内容
     * @param privateKey 私钥
     */
    public static String encryptPri(String content, String privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.encryptBase64(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     */
    public static String encryptPub(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param content   要解密的内容
     * @param publicKey 公钥
     */
    public static String decryptPub(String content, String publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.decryptStr(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥加密
     *
     * @param content   要加密的内容
     * @param publicKey 公钥
     */
    public static String encrypt(String content, PublicKey publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.encryptBase64(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥解密
     *
     * @param content
     * @param publicKey
     * @return
     */
    public static String decrypt(String content, PublicKey publicKey) {
        try {
            RSA rsa = new RSA(null, publicKey);
            return rsa.decryptStr(content, KeyType.PublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥加密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String encrypt(String content, PrivateKey privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.encryptBase64(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content    要解密的内容
     * @param privateKey 私钥
     */
    public static String decrypt(String content, PrivateKey privateKey) {
        try {
            RSA rsa = new RSA(privateKey, null);
            return rsa.decryptStr(content, KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取公私钥-请获取一次后保存公私钥使用
     *
     * @return
     */
    public static Map<String, String> generateKeyPair() {
        try {
            KeyPair pair = SecureUtil.generateKeyPair(ENCRYPT_TYPE);
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            // 获取 公钥和私钥 的 编码格式（通过该 编码格式 可以反过来 生成公钥和私钥对象）
            byte[] pubEncBytes = publicKey.getEncoded();
            byte[] priEncBytes = privateKey.getEncoded();

            // 把 公钥和私钥 的 编码格式 转换为 Base64文本 方便保存
            String pubEncBase64 = Base64Encoder.encode(pubEncBytes);
            String priEncBase64 = Base64Encoder.encode(priEncBytes);
            Map<String, String> map = new HashMap<String, String>(2);
            map.put(PUBLIC_KEY, pubEncBase64);
            map.put(PRIVATE_KEY, priEncBase64);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(JSONObject.toJSONString(generateKeyPair()));
    }

}
