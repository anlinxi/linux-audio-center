package com.faker.audioStation.strategies.wyyApi;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.mapper.LyricMapper;
import com.faker.audioStation.mapper.MusicCoverMapper;
import com.faker.audioStation.mapper.MvMapper;
import com.faker.audioStation.mapper.SingerMapper;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

    @Autowired
    @ApiModelProperty("缓存服务")
    protected CacheService cacheService;

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

    /**
     * 网易云方法调用入口
     *
     * @param params
     * @return
     */
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
            Wrapper<JSONObject> wrapper = this.doSomeThing(params);
            log.info("策略执行结果:" + wrapper);
            if (wrapper.success() && null != wrapper.getResult()) {
                cacheService.set(key, wrapper.getResult().toJSONString(), 8, TimeUnit.HOURS);
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
    public abstract Wrapper<JSONObject> doSomeThing(WyyApiDto params);
}
