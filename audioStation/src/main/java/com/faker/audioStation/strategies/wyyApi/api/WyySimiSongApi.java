package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.core.io.file.FileReader;
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
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 相似音乐策略
 */
@Slf4j
@Component
public class WyySimiSongApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/simi/song";

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
        JSONObject resultJson = super.getHttp(params);
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
        String id = ToolsUtil.getString(params.getData().get("id"));
        JSONObject form = new JSONObject();
        form.put("songid", id);
        this.setFormInteger("limit", 50, form, params.getData());
        this.setFormInteger("offset", 0, form, params.getData());
        String result = wyyHttpUtil.httpContent(WyyApiTypeEnum.WE_API, Method.POST, PROTOCOL + "music.163.com/weapi/v1/discovery/simiSong", form);
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
        params.setMethod("get");
        params.setUrl("/simi/song?id=2029174219");
//        Wrapper wyyWrap = wyyApiTest(params);
//        log.debug(wyyWrap.toString());
        Wrapper wrapper = this.runTest(params);
        log.info("测试结果:" + wrapper);
    }
}
