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

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 音乐详情策略
 */
@Slf4j
@Component
public class WyySongDetailApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/song/detail";

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
        Map<String, String> urlQuery = ToolsUtil.parseUrlQuery(params.getUrl());
        String id = urlQuery.get("id");
        JSONObject form = new JSONObject();
        JSONArray ids = new JSONArray();
        Arrays.asList(id.split(",")).forEach(id2 -> {
            JSONObject idJson = new JSONObject();
            idJson.put("id", id2);
            ids.add(idJson);
        });

        form.put("c", ids.toJSONString());
        form.put("csrf_token", "");
        String result = wyyHttpUtil.httpContent(WyyApiTypeEnum.WE_API, Method.POST, PROTOCOL + "music.163.com/weapi/v3/song/detail", form);
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
        params.setUrl("/song/detail?id=1815918804,1806926997,190072,1815692964,1494807967,1816716764,1814090981,1806732446,1496392700,1486060211,1492319441,416552313,296885,1492319432,412902075,1813812305,1498342485,1479706965,1804879213,29764634,1330348068,1808492017,64093,1495058484,5249197,1331593956,108284,255858,1494510428,5261904");
//        Wrapper wyyWrap = wyyApiTest(params);
//        log.debug(wyyWrap.toString());
        Wrapper wrapper = this.runTest(params);
        log.info("测试结果:" + wrapper);
    }
}
