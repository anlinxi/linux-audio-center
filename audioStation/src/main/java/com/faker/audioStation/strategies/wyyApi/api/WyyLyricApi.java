package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.enums.WyyApiTypeEnum;
import com.faker.audioStation.model.domain.Lyric;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 歌词策略
 */
@Slf4j
@Component
public class WyyLyricApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/lyric";

    @ApiModelProperty("定义的网易云请求方法")
    protected Method method = Method.GET;


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
            QueryWrapper<Lyric> queryWrapper = new QueryWrapper();
            queryWrapper.eq("WYY_ID", id);
            Lyric lyric = lyricMapper.selectOne(queryWrapper);
            if (null != lyric) {
                FileReader reader = new FileReader(lyric.getPath());
                JSONObject result = new JSONObject();
                JSONObject lrc = new JSONObject();
                lrc.put("lyric", reader.readString());
                result.put("code", 200);
                result.put("lrc", lrc);
                result.put("lyric", lyric);
                WrapMapper.ok(result);
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
    public JSONObject getWyyHttp(WyyApiDto params) throws Exception{
        Map<String, String> urlQuery = ToolsUtil.parseUrlQuery(params.getUrl());
        String id = urlQuery.get("id");
        JSONObject form = new JSONObject();
        form.put("id", id);
        form.put("tv", -1);
        form.put("lv", -1);
        form.put("rv", id);
        form.put("kv", id);
        String result = wyyHttpUtil.httpContent(WyyApiTypeEnum.WE_API, Method.POST, "http://music.163.com/api/song/lyric?_nmclfl=1", form);
        log.debug(result);
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
        params.setUrl("/lyric?id=29850683");
        Wrapper wrapper = this.runTest(params);
        log.info("测试结果:" + wrapper);
    }
}
