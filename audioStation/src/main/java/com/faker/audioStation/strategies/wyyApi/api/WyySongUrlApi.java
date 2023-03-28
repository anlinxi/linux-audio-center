package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.service.DownloadService;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 歌词策略
 */
@Slf4j
@Component
public class WyySongUrlApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/song/url";

    @ApiModelProperty("定义的网易云请求方法")
    protected Method method = Method.GET;

    @Autowired
    @ApiModelProperty("下载服务")
    protected DownloadService downloadService;


    /**
     * 获取定义的url
     *
     * @return
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * 获取请求类型 post/get
     *
     * @return
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * 个性化的策略执行内容
     *
     * @param params
     * @return
     */
    @Override
    public Wrapper<JSONObject> doSomeThing(WyyApiDto params) {
        String key = SecureUtil.md5(JSONObject.toJSONString(params));
        Map<String, String> urlQuery = ToolsUtil.parseUrlQuery(params.getUrl());
        if (null != urlQuery.get("id")) {
            String id = urlQuery.get("id");
            QueryWrapper<Music> queryWrapper = new QueryWrapper();
            queryWrapper.eq("WYY_ID", id);
            Music music = musicMapper.selectOne(queryWrapper);
            if (null == music) {
                String url = music163Api + "/song/url";
                log.info("网易云音乐api请求地址:" + url);
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("id", id);
                String resultText = HttpUtil.get(url, paramsMap);
                if (null == resultText) {
                    return null;
                }
                if (resultText.length() < 100) {
                    log.info("网易云音乐api返回:" + resultText);
                }
                SongUrlRootBean songUrlRootBean = JSONObject.parseObject(resultText, SongUrlRootBean.class);
                if (songUrlRootBean.getData().get(0).getFee() != 1) {
                    new Thread(() -> {
                        SongUrlRootBean songUrlRootBeanV2 = downloadService.downLoadMusic(songUrlRootBean);
                        cacheService.set(key, JSONObject.toJSONString(songUrlRootBeanV2), 4, TimeUnit.HOURS);
                    }).start();
                    return WrapMapper.ok(JSONObject.parseObject(resultText));
                }

            } else {
                SongUrlRootBean songUrlRootBean = new SongUrlRootBean(music);
                cacheService.set(key, JSONObject.toJSONString(songUrlRootBean), 4, TimeUnit.HOURS);
                return WrapMapper.ok(JSONObject.parseObject(JSONObject.toJSONString(songUrlRootBean)));
            }
        }
        JSONObject resultJson = super.callWyyAPi(params);
        cacheService.set(key, resultJson.toJSONString(), 8, TimeUnit.HOURS);
        return WrapMapper.ok(resultJson);
    }

}
