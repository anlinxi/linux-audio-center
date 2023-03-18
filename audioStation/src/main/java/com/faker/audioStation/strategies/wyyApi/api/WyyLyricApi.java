package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.model.domain.Lyric;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public Wrapper doSomeThing(WyyApiDto params) {
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
        return WrapMapper.ok(super.callWyyAPi(params));
    }
}
