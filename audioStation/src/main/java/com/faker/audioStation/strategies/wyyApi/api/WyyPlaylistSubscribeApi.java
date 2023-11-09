package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.mapper.PlaylistSubscribeMapper;
import com.faker.audioStation.model.domain.PlaylistSubscribe;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>收藏歌单</p>
 *
 * <p>项目名称：linux-audio-center</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/9/28</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/9/28 10:22
 */
@Slf4j
@Component
public class WyyPlaylistSubscribeApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/playlist/subscribe";

    @ApiModelProperty("定义的网易云请求方法")
    protected Method method = Method.GET;

    @ApiModelProperty("歌单收藏信息mapper")
    @Autowired
    private PlaylistSubscribeMapper playlistSubscribeMapper;

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
        String userId = params.getUserId();

        PlaylistSubscribe playSubscribe = playlistSubscribeMapper.selectById(id);
        if (playSubscribe == null) {
            playSubscribe.setPlaylistId(Long.parseLong(id));
            playSubscribe.setUserId(userId);
            playSubscribe.setCreateTime(new Date());
            playlistSubscribeMapper.insert(playSubscribe);
        } else {
            playlistSubscribeMapper.deleteById(playSubscribe.getPlaylistId());
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("code", 200);
        resultJson.put("msg", "操作成功");
        return resultJson;
    }

    /**
     * 测试方法
     *
     * @return
     */
    @Test
    public void test() {

    }
}
