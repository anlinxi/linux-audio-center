package com.faker.audioStation.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.enums.WyyApiTypeEnum;
import com.faker.audioStation.exception.NoMoneyToEatKFCException;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
@Component
@Slf4j
public class WyyHttpUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @ApiModelProperty("pc的userAgent")
    private String[] userAgentPc = new String[]{"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:80.0) Gecko/20100101 Firefox/80.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.30 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586"};

    @ApiModelProperty("手机的userAgent")
    private String[] userAgentApp = new String[]{"Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML like Gecko) Mobile/14A456 QQ/6.5.7.408 V1_IPH_SQ_6.5.7_1_APP_A Pixel/750 Core/UIWebView NetType/4G Mem/103",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.15(0x17000f27) NetType/WIFI Language/zh",
            "Mozilla/5.0 (Linux; Android 9; PCT-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.64 HuaweiBrowser/10.0.3.311 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 9; zh-cn; Redmi Note 8 Build/PKQ1.190616.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.141 Mobile Safari/537.36 XiaoMi/MiuiBrowser/12.5.22",
            "Mozilla/5.0 (Linux; Android 10; YAL-AL00 Build/HUAWEIYAL-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.62 XWEB/2581 MMWEBSDK/200801 Mobile Safari/537.36 MMWEBID/3027 MicroMessenger/7.0.18.1740(0x27001235) Process/toolsmp WeChat/arm64 NetType/WIFI Language/zh_CN ABI/arm64",
            "Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BKK-AL10 Build/HONORBKK-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/10.6 Mobile Safari/537.36"};

    @ApiModelProperty("未登录token")
    private String anonymousToken = "bf8bfeabb1aa84f9c8c3906c04a04fb864322804c83f5d607e91a04eae463c9436bd1a17ec353cf780b396507a3f7464e8a60f4bbc019437993166e004087dd32d1490298caf655c2353e58daa0bc13cc7d5c198250968580b12c1b8817e3f5c807e650dd04abd3fb8130b7ae43fcc5b";

    @ApiModelProperty(value = "aes加密秘钥", notes = "weapi的秘钥")
    private final String ENCODE_KEY = "0CoJUm6Qyw8W8jud";

    @ApiModelProperty("aes加密偏移量")
    private final String IV_KEY = "0102030405060708";

    @ApiModelProperty("base62")
    private final String base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @ApiModelProperty("RSA公钥")
    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6sXqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9SdB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";

    @ApiModelProperty(value = "aes加密秘钥", notes = "eapi的秘钥")
    private final String EAPI_KEY = "e82ckenh8dichen8";

    @ApiModelProperty(value = "aes加密秘钥", notes = "linuxApi的秘钥")
    private final String LINUX_API_KEY = "rFgB&h#%2?^eDg:Q";

    @Value("${faker.unblockNeteaseMusic.proxy:}")
    @ApiModelProperty("解锁网易云灰色音乐的代理")
    private String unblockNeteaseMusicProxy;

    public String getUnblockNeteaseMusicProxy() {
        return unblockNeteaseMusicProxy;
    }

    public void setUnblockNeteaseMusicProxy(String unblockNeteaseMusicProxy) {
        this.unblockNeteaseMusicProxy = unblockNeteaseMusicProxy;
    }

    /**
     * 歌曲详情
     *
     * @throws Exception
     */
    @Test
    public void songDetail() throws Exception {
        JSONObject form = new JSONObject();
        JSONArray ids = new JSONArray();
        JSONObject id = new JSONObject();
        id.put("id", 29850683);
        ids.add(id);
        form.put("c", ids.toJSONString());
        form.put("csrf_token", "");
        String result = this.httpContent(WyyApiTypeEnum.WE_API, Method.POST, "http://music.163.com/weapi/v3/song/detail", form);
        log.info(result);
    }

    /**
     * 歌曲下载地址
     *
     * @throws Exception
     */
    @Test
    public void songUrl() throws Exception {
        JSONObject form = new JSONObject();
        JSONArray ids = new JSONArray();
        ids.add(29850683);
        form.put("ids", ids.toJSONString());
        form.put("br", 999000);
        String result = this.httpContent(WyyApiTypeEnum.E_API, Method.POST, "http://interface3.music.163.com/eapi/song/enhance/player/url", form);
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
    public String httpContent(WyyApiTypeEnum wyyApiTypeEnum, Method method, String url, JSONObject form) throws Exception {
        String text = form.toJSONString();
        log.debug("原始json字符串:" + text);
        if (WyyApiTypeEnum.WE_API.equals(wyyApiTypeEnum)) {
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
            return this.httpContent(method, url, formMap, this.getHeaders(wyyApiTypeEnum));
        } else if (WyyApiTypeEnum.LINUX_API.equals(wyyApiTypeEnum)) {
//            String params = AesPkcs7PaddingUtil.encryptHex(text, LINUX_API_KEY, null).toUpperCase();
            AES ase = new AES("ECB", "PKCS7Padding", LINUX_API_KEY.getBytes());
            String params = ase.encryptHex(text).toUpperCase();
            Map<String, Object> formMap = new HashMap<>(1);
            formMap.put("eparams", params);
            return this.httpContent(method, url, formMap, this.getHeaders(wyyApiTypeEnum));
        } else if (WyyApiTypeEnum.E_API.equals(wyyApiTypeEnum)) {
            String apiUrl = "/api" + url.split(wyyApiTypeEnum.getName())[1];
            Map<String, String> headersParams = new LinkedHashMap<>();
            headersParams.put("appver", "8.7.01");
            headersParams.put("versioncode", "140");
//            headersParams.put("mobilename", "xiao mi 10 pro");
            headersParams.put("buildver", String.valueOf(System.currentTimeMillis()).substring(0, 10));
            headersParams.put("resolution", "1920x1080");
            headersParams.put("__csrf", "");
            headersParams.put("os", "pc");
            DecimalFormat df1 = new DecimalFormat("0000");
            headersParams.put("requestId", System.currentTimeMillis() + "_" + df1.format(RandomUtil.randomInt(0, 9999)));
            headersParams.put("MUSIC_A", anonymousToken);
            form.put("header", headersParams);
            //{"ids":"[29850683]","br":999000,"header":{"appver":"8.7.01","versioncode":"140","buildver":"1680107114","resolution":"1920x1080","__csrf":"","os":"pc","requestId":"1680107114839_0575","MUSIC_A":"bf8bfeabb1aa84f9c8c3906c04a04fb864322804c83f5d607e91a04eae463c9436bd1a17ec353cf780b396507a3f7464e8a60f4bbc019437993166e004087dd32d1490298caf655c2353e58daa0bc13cc7d5c198250968580b12c1b8817e3f5c807e650dd04abd3fb8130b7ae43fcc5b"}}
            text = form.toJSONString();
            String message = "nobody" + apiUrl + "use" + text + "md5forencrypt";
            String digest = SecureUtil.md5(message);
            String data = apiUrl + "-36cd479b6b5-" + text + "-36cd479b6b5-" + digest;
            log.debug("data=" + data);
            AES ase = new AES("ECB", "PKCS7Padding", EAPI_KEY.getBytes());
            String params = ase.encryptHex(data).toUpperCase();
            log.debug("params=" + params);
            Map<String, Object> formMap = new HashMap<>(1);
            formMap.put("params", params);
            return this.httpContent(method, url, formMap, this.getHeaders(wyyApiTypeEnum));
        } else {
            throw new NoMoneyToEatKFCException("未定义的api" + wyyApiTypeEnum);
        }
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
    public String httpContent(Method method, String url, Map<String, Object> formMap, Map<String, String> headers) throws Exception {
        Proxy proxy = null;
        if (ToolsUtil.isNotNull(unblockNeteaseMusicProxy) && unblockNeteaseMusicProxy.contains(":")) {
            try {
                String[] unblockNeteaseMusicProxyArr = unblockNeteaseMusicProxy.split(":");
                String proxyIp = unblockNeteaseMusicProxyArr[0];
                Integer proxyPort = Integer.parseInt(unblockNeteaseMusicProxyArr[1]);
                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpResponse response = null;
        if (Method.GET.equals(method)) {
            log.debug("[" + method + "]请求地址[" + url + "]发送参数[" + formMap + "]");
            response = HttpUtil.createGet(url).form(formMap).setProxy(proxy).headerMap(headers, true)
                    .setSSLSocketFactory(SSLSocketFactoryUtil.createSslSocketFactory()).executeAsync();
        } else if (Method.POST.equals(method)) {
            String body = this.getBodyByMap(formMap);
            log.debug("[" + method + "]请求地址[" + url + "]发送参数[" + body + "]");
            response = HttpUtil.createPost(url).form(formMap).body(body).setProxy(proxy)
                    .headerMap(headers, true).setSSLSocketFactory(SSLSocketFactoryUtil.createSslSocketFactory()).execute();
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
    public String getUserAgentPc() {
        int i = RandomUtil.randomInt(0, userAgentPc.length);
        return userAgentPc[i];
    }

    /**
     * 获取随机userAgent
     *
     * @return
     */
    public String getUserAgentApp() {
        int i = RandomUtil.randomInt(0, userAgentApp.length);
        return userAgentApp[i];
    }

    /**
     * 请求头
     *
     * @param wyyApiTypeEnum
     * @return
     */
    public Map<String, String> getHeaders(WyyApiTypeEnum wyyApiTypeEnum) {
        Map<String, String> headers = new HashMap<String, String>();
        if (WyyApiTypeEnum.WE_API.equals(wyyApiTypeEnum)) {
            headers.put("User-Agent", this.getUserAgentPc());
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Referer", "http://music.163.com");
            headers.put("X-Real-IP", "127.0.0.1");
            headers.put("X-Forwarded-For", "127.0.0.1");
            headers.put("Cookie", this.getCookie(wyyApiTypeEnum));
        } else if (WyyApiTypeEnum.LINUX_API.equals(wyyApiTypeEnum)) {

        } else if (WyyApiTypeEnum.E_API.equals(wyyApiTypeEnum)) {
            headers.put("User-Agent", this.getUserAgentApp());
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Referer", "http://music.163.com");
            headers.put("X-Real-IP", "127.0.0.1");
            headers.put("X-Forwarded-For", "127.0.0.1");
            headers.put("Cookie", this.getCookie(wyyApiTypeEnum));
        } else {
            throw new NoMoneyToEatKFCException("未定义的api" + wyyApiTypeEnum);
        }

        log.debug("请求头: " + headers);
        return headers;
    }

    /**
     * 获取Cookie
     *
     * @param wyyApiTypeEnum
     * @return
     */
    public String getCookie(WyyApiTypeEnum wyyApiTypeEnum) {
        StringBuffer cookie = new StringBuffer();
        Map<String, String> cookieMap = this.getCookieMap(wyyApiTypeEnum);

        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            cookie.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        if (cookie.length() > 2) {
            cookie.setLength(cookie.length() - 2);
        }
        return cookie.toString();
    }

    /**
     * 获取CookieMap
     *
     * @param wyyApiTypeEnum
     * @return
     */
    public Map<String, String> getCookieMap(WyyApiTypeEnum wyyApiTypeEnum) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        if (WyyApiTypeEnum.WE_API.equals(wyyApiTypeEnum)) {
            cookieMap.put("__remember_me", "true");
            cookieMap.put("NMTID", RandomUtil.randomString(16));
            cookieMap.put("_ntes_nuid", RandomUtil.randomString(16));
            cookieMap.put("MUSIC_A", anonymousToken);
        } else if (WyyApiTypeEnum.LINUX_API.equals(wyyApiTypeEnum)) {

        } else if (WyyApiTypeEnum.E_API.equals(wyyApiTypeEnum)) {
            cookieMap.put("osver", "undefined");
            cookieMap.put("deviceId", "undefined");
            cookieMap.put("appver", "8.7.01");
            cookieMap.put("versioncode", "140");
            cookieMap.put("mobilename", "xiao mi 10 pro");
            cookieMap.put("buildver", String.valueOf(System.currentTimeMillis()).substring(0, 10));
            cookieMap.put("resolution", "1920x1080");
            cookieMap.put("__csrf", "");
            cookieMap.put("os", "pc");
            cookieMap.put("channel", "undefined");
            DecimalFormat df1 = new DecimalFormat("0000");
            cookieMap.put("requestId", System.currentTimeMillis() + "_" + df1.format(RandomUtil.randomInt(0, 9999)));
            cookieMap.put("channel", "undefined");
            cookieMap.put("MUSIC_A", anonymousToken);
        } else {
            throw new NoMoneyToEatKFCException("未定义的api" + wyyApiTypeEnum);
        }
        return cookieMap;
    }
}
