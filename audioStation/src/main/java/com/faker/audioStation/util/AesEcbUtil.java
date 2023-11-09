//package com.faker.audioStation.util;
//
//import cn.hutool.core.codec.Base64Decoder;
//import cn.hutool.core.codec.Base64Encoder;
//import cn.hutool.core.util.HexUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.junit.Test;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//import javax.validation.constraints.NotNull;
//import java.security.Security;
//
///**
// * <p>AesPkcs7PaddingUtil</p>
// *
// * <p>项目名称：linux-audio-center</p>
// *
// * <p>注释:无</p>
// *
// * <p>Copyright: Copyright Faker(c) 2023/3/29</p>
// *
// * <p>公司: Faker</p>
// *
// * @author 淡梦如烟
// * @version 1.0
// * @date 2023/5/25 11:29
// */
//@Slf4j
//public class AesEcbUtil {
//
//    /**
//     * 编码格式
//     */
//    private static final String CHARSET_NAME = "UTF-8";
//    /**
//     * 加密方式
//     */
//    private static final String AES_NAME = "AES";
//    /**
//     * 加密模式
//     */
//    public static final String ALGORITHM = "AES/ECB/PKCS5Padding";
//
//
//    @Test
//    public void test() throws Exception {
//        String json = "6E932DF280943066C6EFF1385C1CC5EA";
//        log.info(json);
//        String data = AesEcbUtil.encryptHex("15909068106", "XXoZw8aegeerrli5");
//        log.info(data);
//        String result = AesEcbUtil.decryptHex(json, "XXoZw8aegeerrli5");
//        log.info(result);
//
//    }
//
//
//    /**
//     * 加密
//     *
//     * @param content
//     * @param key
//     * @return
//     */
//    public static String encrypt(@NotNull String content, @NotNull String key) {
//        byte[] result = null;
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
//            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
//            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Base64Encoder.encode(result);
//    }
//
//    /**
//     * 加密
//     *
//     * @param content
//     * @param key
//     * @return
//     */
//    public static String encryptHex(@NotNull String content, @NotNull String key) {
//        byte[] result = null;
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
//            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
//            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return HexUtil.encodeHexStr(result);
//    }
//
//    /**
//     * 解密
//     *
//     * @param content
//     * @param key
//     * @return
//     */
//    public static String decrypt(@NotNull String content, @NotNull String key) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
//            cipher.init(Cipher.DECRYPT_MODE, keySpec);
//            return new String(cipher.doFinal(Base64Decoder.decode(content)), CHARSET_NAME);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 解密
//     *
//     * @param content
//     * @param key
//     * @return
//     */
//    public static String decryptHex(@NotNull String content, @NotNull String key) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
//            cipher.init(Cipher.DECRYPT_MODE, keySpec);
//            return new String(cipher.doFinal(HexUtil.decodeHex(content)), CHARSET_NAME);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
