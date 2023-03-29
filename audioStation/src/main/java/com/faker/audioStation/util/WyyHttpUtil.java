package com.faker.audioStation.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.exception.NoMoneyToEatKFCException;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import javax.crypto.Cipher;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>WyyHttpUtil</p>
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
 * @date 2023/3/29 10:00
 */
@Slf4j
public class WyyHttpUtil {

    @ApiModelProperty("userAgent")
    private String[] userAgent = new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:80.0) Gecko/20100101 Firefox/80.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586"};

    @ApiModelProperty("未登录token")
    private String anonymousToken = "bf8bfeabb1aa84f9c8c3906c04a04fb864322804c83f5d607e91a04eae463c9436bd1a17ec353cf780b396507a3f7464e8a60f4bbc019437993166e004087dd32d1490298caf655c2353e58daa0bc13cc7d5c198250968580b12c1b8817e3f5c807e650dd04abd3fb8130b7ae43fcc5b";

    @ApiModelProperty("aes加密秘钥")
    private final String ENCODE_KEY = "0CoJUm6Qyw8W8jud";

    @ApiModelProperty("aes加密偏移量")
    private final String IV_KEY = "0102030405060708";

    @ApiModelProperty("base62")
    private final String base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @ApiModelProperty("RSA公钥")
    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";

    @Test
    public void test() throws Exception {
        JSONObject form = new JSONObject();
        JSONArray ids = new JSONArray();
        JSONObject id = new JSONObject();
        id.put("id", 29850683);
        ids.add(id);
        form.put("c", ids.toJSONString());
        form.put("csrf_token", "");
        String result = this.httpContent(Method.POST, "http://music.163.com/weapi/v3/song/detail", form);
        log.info(result);
    }

    /**
     * 开始加密请求
     *
     * @param method
     * @param url
     * @param form
     * @return
     */
    public String httpContent(Method method, String url, JSONObject form) throws Exception {
        String text = form.toJSONString();
        //获取随机16位秘钥
        String secretKey = this.getSecretKey();

        //一次aes加密
        String aesText1 = AesPkcs7PaddingUtil.encrypt(text, ENCODE_KEY, IV_KEY);

        //二次aes加密
        String params = AesPkcs7PaddingUtil.encrypt(aesText1, secretKey, IV_KEY);

        //准备生成rsa加密参数
        String secretKeyReverse = new StringBuffer(secretKey).reverse().toString();
        int byteLong = 128;
        byte[] secretKeyReverseByte = new byte[byteLong];
        for (int i = 0; i < (byteLong - secretKeyReverse.length()); i++) {
            secretKeyReverseByte[i] = 0;
        }
        byte[] tmpSecretKey = secretKeyReverse.getBytes();
        for (int i = (byteLong - secretKeyReverse.length()); i < byteLong; i++) {
            secretKeyReverseByte[i] = tmpSecretKey[i - (byteLong - secretKeyReverse.length())];
        }
        //rsa加密
        String encSecKey = HexUtil.encodeHexStr(this.encryptByPubKey(secretKeyReverseByte, Base64.decodeBase64(PUBLIC_KEY)));

        Map<String, Object> formMap = new HashMap<>(2);
        formMap.put("params", params);
        formMap.put("encSecKey", encSecKey);
        return this.httpContent(method, url, formMap);

    }

    /**
     * 公钥加密
     *
     * @param data   待加密数据
     * @param pubKey 公钥
     * @return
     * @throws Exception
     */
    public byte[] encryptByPubKey(byte[] data, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 生成秘钥
     *
     * @return
     */
    public String getSecretKey() {
        return RandomUtil.randomString(base62, 16);
    }

    /**
     * 开始请求
     *
     * @param method
     * @param url
     * @param formMap
     * @return
     * @throws Exception
     */
    public String httpContent(Method method, String url, Map<String, Object> formMap) throws Exception {
        Proxy proxy = null;
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.123.223", 33335));
        HttpResponse response = null;
        if (Method.GET.equals(method)) {
            log.info("[" + method + "]请求地址[" + url + "]发送参数[" + formMap + "]");
            response = HttpUtil.createGet(url).form(formMap).setProxy(proxy).headerMap(this.getHeaders(), true)
                    .setSSLSocketFactory(SSLSocketFactoryUtil.createSslSocketFactory()).executeAsync();
        } else if (Method.POST.equals(method)) {
            String body = this.getBodyByMap(formMap);
            log.info("[" + method + "]请求地址[" + url + "]发送参数[" + body + "]");
            response = HttpUtil.createPost(url).form(formMap).body(body).setProxy(proxy)
                    .headerMap(this.getHeaders(), true).setSSLSocketFactory(SSLSocketFactoryUtil.createSslSocketFactory()).execute();
        } else {
            throw new NoMoneyToEatKFCException("未定义的方法[" + method + "]");
        }
        String searchText = response.body();
        return searchText;
    }

    /**
     * 转换body
     *
     * @param formMap
     * @return
     */
    private String getBodyByMap(Map<String, Object> formMap) {
        StringBuffer body = new StringBuffer();
        for (Map.Entry<String, Object> entry : formMap.entrySet()) {
            body.append(entry.getKey()).append("=").append(URLUtil.encodeAll(ToolsUtil.getString(entry.getValue()), CharsetUtil.CHARSET_UTF_8)).append("&");
        }
        if (body.length() > 1) {
            body.setLength(body.length() - 1);
        }
        return body.toString();
    }

    /**
     * 获取随机userAgent
     *
     * @return
     */
    public String getUserAgent() {
        int i = RandomUtil.randomInt(0, userAgent.length);
        return userAgent[i];
    }

    /**
     * 请求头
     *
     * @return
     */
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", this.getUserAgent());
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Referer", "http://music.163.com");
        headers.put("X-Real-IP", "127.0.0.1");
        headers.put("X-Forwarded-For", "127.0.0.1");
        headers.put("Cookie", this.getCookie());
        log.debug("请求头: " + headers);
        return headers;
    }

    /**
     * 获取Cookie
     *
     * @return
     */
    public String getCookie() {
        StringBuffer cookie = new StringBuffer();
        Map<String, String> cookieMap = new HashMap<String, String>();
        cookieMap.put("__remember_me", "true");
        cookieMap.put("NMTID", RandomUtil.randomString(16));
        cookieMap.put("_ntes_nuid", RandomUtil.randomString(16));
        cookieMap.put("MUSIC_A", anonymousToken);

        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            cookie.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        if (cookie.length() > 2) {
            cookie.setLength(cookie.length() - 2);
        }
        return cookie.toString();
    }

}
