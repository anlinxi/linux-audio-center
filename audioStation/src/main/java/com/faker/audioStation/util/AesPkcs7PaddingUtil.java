package com.faker.audioStation.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * <p>AesPkcs7PaddingUtil</p>
 *
 * <p>项目名称：linux-audio-center</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/29</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/29 15:29
 */
@Slf4j
public class AesPkcs7PaddingUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 编码格式
     */
    private static final String CHARSET_NAME = "UTF-8";
    /**
     * 加密方式
     */
    private static final String AES_NAME = "AES";
    /**
     * 加密模式
     */
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * RSA加密方式
     */
    public static final String RSA_ALGORITHM = "RSA";


    @Test
    public void test() throws Exception {
        JSONObject form = new JSONObject();
        JSONArray ids = new JSONArray();
        JSONObject id = new JSONObject();
        id.put("id", 29850683);
        ids.add(id);
        form.put("c", ids.toJSONString());
        form.put("csrf_token", "");
        String json = form.toJSONString();
        log.info(json);
//        json = "{\"c\":\"[{\\\"id\\\":29850683}]\",\"csrf_token\":\"\"}";
        log.info(json);
        String result = AesPkcs7PaddingUtil.encrypt(json, "0CoJUm6Qyw8W8jud", "0102030405060708");
        log.info(result);
        log.info("5UVzkuTUF1XlbGqn4UD2dPRMd6jQT5jSRZSDYx5Eq4J0fpl2ABAG77TuezbU7bx+");


        String encSecKey = AesPkcs7PaddingUtil.encrypt(result, "jH7pk9jq099zjzuV", "0102030405060708");
        log.info(encSecKey);
        log.info("XG6Ecvchu3hb8q+OeCudFV7SkaAeOLkM3qEIHMNyxw+HCw1HxAaW0caGfOj2Ag7FmDhGqdgQwi3g7nNMPg0t0IyH4Tnff2Y5eeaAvFYUqFw=");
        log.info(URLUtil.encodeAll(encSecKey));
        log.info("XG6Ecvchu3hb8q%2BOeCudFV7SkaAeOLkM3qEIHMNyxw%2BHCw1HxAaW0caGfOj2Ag7FmDhGqdgQwi3g7nNMPg0t0IyH4Tnff2Y5eeaAvFYUqFw%3D");

    }

    @Test
    public void rsaTest() throws Exception {
        String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";
        String secretKeyReverse = new StringBuffer("jH7pk9jq099zjzuV").reverse().toString();
        log.info(secretKeyReverse);
        int byteLong = 128;
        byte[] secretKeyReverseByte = new byte[byteLong];
        for (int i = 0; i < (byteLong - secretKeyReverse.length()); i++) {
            secretKeyReverseByte[i] = 0;
        }
        byte[] tmpSecretKey = secretKeyReverse.getBytes();
        for (int i = (byteLong - secretKeyReverse.length()); i < byteLong; i++) {
            secretKeyReverseByte[i] = tmpSecretKey[i - (byteLong - secretKeyReverse.length())];
        }
        log.info(Arrays.toString(secretKeyReverseByte));
//        RSA rsa = new RSA(null, PUBLIC_KEY);
//        String encSecKey = rsa.encryptHex(secretKeyReverseByte, KeyType.PublicKey);

        String encSecKey = AesPkcs7PaddingUtil.publicEncrypt(secretKeyReverseByte, PUBLIC_KEY);

        log.info(encSecKey);
        log.info("74582dc679bec31d6279e50bcbed931756c8de354187ba51ee35a2abef917153441215fac2588c8efd605348a4dab202a847fa69f62f5610457d8e35a08a3db00aeaedc758f6112c25c236bdee1745650012cbc9ed3f064d0e5de9b2caf2ebd1e898d6ee059e03ce9097218cd2f5342ac4d045b04755eb4b47a228283e7fe2c4");

    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(byte[] data, String publicKey) {
        try {
            // 通过X509编码的Key指令获得公钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            byte[] resultByte = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, rsaPublicKey.getModulus().bitLength());
//            return Base64.encodeBase64URLSafeString(resultByte);
            return HexUtil.encodeHexStr(resultByte);
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
     *
     * @param cipher
     * @param opmode
     * @param datas
     * @param keySize
     * @return
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        //最大块
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }


    /**
     * 加密
     *
     * @param content
     * @param key
     * @param vi
     * @return
     */
    public static String encrypt(@NotNull String content, @NotNull String key, String vi) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            if (null != vi) {
                AlgorithmParameterSpec paramSpec = new IvParameterSpec(vi.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            }
            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64Encoder.encode(result);
    }

    /**
     * 加密
     *
     * @param content
     * @param key
     * @param vi
     * @return
     */
    public static String encryptHex(@NotNull String content, @NotNull String key, String vi) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            if (null != vi) {
                AlgorithmParameterSpec paramSpec = new IvParameterSpec(vi.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            }
            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HexUtil.encodeHexStr(result);
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @param vi
     * @return
     */
    public static String decrypt(@NotNull String content, @NotNull String key, @NotNull String vi) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(vi.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64Decoder.decode(content)), CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
