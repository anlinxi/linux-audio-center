package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.enums.WyyApiTypeEnum;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户播放列表
 */
@Slf4j
@Component
public class WyyUserPlaylistApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/user/playlist";

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
//        Map<String, String> urlQuery = ToolsUtil.parseUrlQuery(params.getUrl());
//        String id = urlQuery.get("id");
//        JSONObject form = new JSONObject();
//        form.put("uid", id);
//        this.setFormInteger("limit", 30, form, urlQuery);
//        this.setFormInteger("offset", 0, form, urlQuery);
//        form.put("includeVideo", true);
//        String result = wyyHttpUtil.httpContent(WyyApiTypeEnum.WE_API, Method.POST, PROTOCOL + "music.163.com/api/user/playlist", form);
//        log.debug(result);
        //todo 以后可以替换成本地保存的播放列表
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", 200);
        resultJson.put("message", "linux音乐中心播放列表");
        resultJson.put("msg", "linux音乐中心播放列表");
        resultJson.put("data", new ArrayList());
        return resultJson;
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
        params.setUrl("/user/playlist?uid=admin&limit=30&offset=0");
//        Wrapper wyyWrap = wyyApiTest(params);
//        log.debug(wyyWrap.toString());
        Wrapper wrapper = this.runTest(params);
        log.info("测试结果:" + wrapper);
    }
}
