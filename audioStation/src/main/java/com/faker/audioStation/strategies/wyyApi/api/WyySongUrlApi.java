package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.enums.WyyApiTypeEnum;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 歌曲下载地址策略
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
        if (null != params.getData().get("id")) {
            String id =ToolsUtil.getString(params.getData().get("id"));
            QueryWrapper<Music> queryWrapper = new QueryWrapper();
            queryWrapper.eq("WYY_ID", id);
            Music music = musicMapper.selectOne(queryWrapper);
            if (null == music) {
                JSONObject resultJson = super.callWyyAPi(params);
                if (null == resultJson) {
                    return null;
                }
                String resultText = resultJson.toJSONString();
                if (resultText.length() < 100) {
                    log.info("网易云音乐api返回:" + resultText);
                }
                SongUrlRootBean songUrlRootBean = JSONObject.parseObject(resultText, SongUrlRootBean.class);

                new Thread(() -> {
                    SongUrlRootBean songUrlRootBeanV2 = downloadService.downLoadMusic(songUrlRootBean);
                    cacheService.set(key, JSONObject.toJSONString(songUrlRootBeanV2), 4, TimeUnit.HOURS);
                }).start();
                if (songUrlRootBean.getData() != null
                        && songUrlRootBean.getData().size() > 0
                        && songUrlRootBean.getData().get(0).getFee() == 1) {
                    //vip歌曲
                    try {
                        return WrapMapper.ok(this.getWyyHttp(params));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return WrapMapper.ok(JSONObject.parseObject(resultText));
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

    /**
     * 执行java直连网易云方法
     *
     * @param params
     * @return
     */
    @Override
    public JSONObject getWyyHttp(WyyApiDto params) throws Exception {
        String id =ToolsUtil.getString(params.getData().get("id"));
        JSONObject form = new JSONObject();
        JSONArray ids = new JSONArray();

        Arrays.asList(id.split(",")).forEach(id2 -> {
            ids.add(id2);
        });
        form.put("ids", ids.toJSONString());
        form.put("br", 999000);
        String result = wyyHttpUtil.httpContent(WyyApiTypeEnum.E_API, Method.POST, PROTOCOL + "interface3.music.163.com/eapi/song/enhance/player/url", form);
        return JSONObject.parseObject(result);
    }

    /**
     * 测试方法
     *
     * @return
     */
    @Test
    public void test() {
        WyyApiDto params = new WyyApiDto();
        params.setMethod("get");
        params.setUrl("/song/url?id=561706828,22488579,565201198,22270622,531180630,569701978,548348764,558935382,558670060,542660411,555302006,549919234,567251001,542660420,558946147,536811737,1328502182,531197541,560556874,550604140,568411974,549927514,22591086,22557796,548347871,533316558,1434092227,533312641,560539555,537913317,560556869,1872395,565387478,536224696,530313105,534883242,569077454,564243671,556869508,556869494,563904831");
        Wrapper wyyWrap = wyyApiProxyTest(params, false);
        log.debug(wyyWrap.toString());
        Wrapper wrapper = this.runTest(params);
        log.info("测试结果:" + wrapper);
    }

}
