package com.faker.audioStation.strategies.wyyApi;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.mapper.*;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.util.WyyHttpUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 网易云api调用抽象类
 */
@Slf4j
public abstract class WyyApiAbstract implements WyyApiStrategies {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "";

    @ApiModelProperty("定义的网易云请求方法")
    protected String method = Method.OPTIONS.name();

    @Value("${faker.music163Api:http://yumbo.top:3000}")
    @ApiModelProperty("网易云音乐API地址")
    protected String music163Api;

    @Value("${faker.resources:/music/}")
    @ApiModelProperty("资源文件路径")
    protected String resourcePath;

    @Value("${faker.unblockNeteaseMusic.proxy:}")
    @ApiModelProperty("解锁网易云灰色音乐的代理")
    private String unblockNeteaseMusicProxy;

    @Autowired
    @ApiModelProperty("缓存服务")
    protected CacheService cacheService;

    @Autowired
    @ApiModelProperty("音乐文件Mapper")
    protected MusicMapper musicMapper;

    @Autowired
    @ApiModelProperty("音乐封面图片文件Mapper")
    protected MusicCoverMapper musicCoverMapper;

    @Autowired
    @ApiModelProperty("歌手Mapper")
    protected SingerMapper singerMapper;

    @Autowired
    @ApiModelProperty("歌词Mapper")
    protected LyricMapper lyricMapper;

    @Autowired
    @ApiModelProperty("Mv信息mapper")
    protected MvMapper mvMapper;

    @ApiModelProperty("网易云音乐api")
    protected NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();

    @Autowired
    @ApiModelProperty("java的网易云音乐直连api")
    protected WyyHttpUtil wyyHttpUtil;

    @ApiModelProperty("请求协议")
    final public static String PROTOCOL = "http://";

    /**
     * 网易云方法调用入口
     *
     * @param params
     * @return
     */
    @Override
    public JSONObject getWyyApi(WyyApiDto params) {
        String key = SecureUtil.md5(JSONObject.toJSONString(params));
        String value = cacheService.get(key);
        if (null != value) {
            try {
                return JSONObject.parseObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            //get请求参数和post参数合并，优先post参数
            Map<String, String> urlQuery = ToolsUtil.parseUrlQuery(params.getUrl());
            Map<String, Object> allParams = new HashMap<String, Object>();
            allParams.putAll(urlQuery);
            allParams.putAll(params.getData());
            params.setData(allParams);
            Wrapper<JSONObject> wrapper = this.doSomeThing(params);
            log.info("策略执行结果:" + wrapper);
            if (wrapper.success() && null != wrapper.getResult()) {
                return wrapper.getResult();
            }
        } catch (Exception e) {
            log.info("策略执行异常:" + e.getMessage());
            e.printStackTrace();
        }
        JSONObject resultJson = this.callWyyAPi(params);
        if (null == resultJson) {
            return null;
        }
        //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
        cacheService.set(key, resultJson.toJSONString(), 8, TimeUnit.HOURS);
        return resultJson;
    }

    /**
     * 调用网易云api
     *
     * @param params
     * @return
     */
    public JSONObject callWyyAPi(WyyApiDto params) {
        String method = params.getMethod().toUpperCase();
        String resultText = null;
        String url = music163Api + params.getUrl();
        log.info("网易云音乐api请求地址:" + url);
        if ("GET".equals(method)) {
            resultText = HttpUtil.get(url, params.getData());
        } else if ("POST".equals(method)) {
            resultText = HttpUtil.get(url, params.getData());
        }
        log.info("网易云音乐api返回:" + resultText);
        return JSONObject.parseObject(resultText);
    }

    /**
     * 个性化的策略执行内容
     *
     * @param params
     * @return
     */
    @Override
    public abstract Wrapper<JSONObject> doSomeThing(WyyApiDto params);

    /**
     * 返回代理对象
     *
     * @return
     */
    public Proxy getProxy() {
        if (ToolsUtil.isNotNull(unblockNeteaseMusicProxy) && unblockNeteaseMusicProxy.contains(":")) {
            String[] unblockNeteaseMusicProxyArr = unblockNeteaseMusicProxy.split(":");
            String proxyIp = unblockNeteaseMusicProxyArr[0];
            Integer proxyPort = Integer.parseInt(unblockNeteaseMusicProxyArr[1]);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
            return proxy;
        }
        return null;
    }

    /**
     * 测试方法
     *
     * @param params
     * @return
     */
    public Wrapper runTest(WyyApiDto params) {
        try {
            if (wyyHttpUtil == null) {
                wyyHttpUtil = new WyyHttpUtil();
                //设定测试的wyy解锁代理地址
                wyyHttpUtil.setUnblockNeteaseMusicProxy("192.168.123.223:33335");
            }
            return WrapMapper.ok(this.getWyyHttp(params));
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
    }

    /**
     * 网易云api测试方法
     *
     * @param params 参数
     * @return
     */
    public Wrapper wyyApiTest(WyyApiDto params) {
        return wyyApiProxyTest(params, true);
    }

    /**
     * 网易云api测试方法
     *
     * @param params    参数
     * @param needProxy 是否需要代理
     * @return
     */
    public Wrapper wyyApiProxyTest(WyyApiDto params, boolean needProxy) {
        try {
            String music163Api = "http://127.0.0.1:3000";
            String url2 = music163Api + params.getUrl();
            if (needProxy) {
                url2 = music163Api + params.getUrl() + "?proxy=http://192.168.123.223:33335";
                if (params.getUrl().contains("?")) {
                    url2 = music163Api + params.getUrl() + "&proxy=http://192.168.123.223:33335";
                }
            }

            Proxy proxy = null;
            HttpResponse response = HttpUtil.createPost(url2).form(params.getData()).setProxy(proxy).executeAsync();
            String searchText = response.body();
            return WrapMapper.ok(searchText);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
    }

    /**
     * 自动选择请求路线
     * 优先java网易云api
     *
     * @param params
     * @return
     */
    public JSONObject getHttp(WyyApiDto params) {
        JSONObject resultJson = null;
        try {
            resultJson = this.getWyyHttp(params);
        } catch (Exception e) {
            e.printStackTrace();
            resultJson = this.callWyyAPi(params);
        }
        return resultJson;
    }


    /**
     * 设置表单数值
     *
     * @param attr         属性名称
     * @param defaultValue 默认值
     * @param form         表单
     * @param urlQuery     查询参数
     */
    public void setFormInteger(String attr, Integer defaultValue, JSONObject form, Map<String, Object> urlQuery) {
        this.setFormInteger(attr, defaultValue, form, ToolsUtil.getString(urlQuery.get(attr)));
    }


    /**
     * 设置表单数值
     *
     * @param attr         属性名称
     * @param defaultValue 默认值
     * @param form         表单
     * @param value        设置值
     */
    public void setFormInteger(String attr, Integer defaultValue, JSONObject form, String value) {
        form.put(attr, defaultValue);
        if (ToolsUtil.isNotNull(value)) {
            try {
                form.put(attr, Integer.parseInt(value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
