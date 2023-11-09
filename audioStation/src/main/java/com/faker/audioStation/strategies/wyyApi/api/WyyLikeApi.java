package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.mapper.LikeMapper;
import com.faker.audioStation.model.domain.Like;
import com.faker.audioStation.model.domain.Music;
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

/**
 * 添加收藏
 */
@Slf4j
@Component
public class WyyLikeApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/like";

    @ApiModelProperty("定义的网易云请求方法")
    protected Method method = Method.GET;

    @Autowired
    @ApiModelProperty("收藏歌曲Mapper")
    protected LikeMapper likeMapper;


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
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", 200);
        resultJson.put("msg", "操作成功");
        if (params.getData().get("id") == null) {
            return WrapMapper.error("无歌曲id");
        }
        String id = ToolsUtil.getString(params.getData().get("id"));
        String like = ToolsUtil.getString(params.getData().get("like"));
        String userId = params.getUserId();
        QueryWrapper<Like> queryCount = new QueryWrapper<>();
        queryCount.eq("USER_ID", userId);
        queryCount.eq("WYY_ID", id);
        int count = likeMapper.selectCount(queryCount);
        if ("true".equals(like) && count > 0) {
            return WrapMapper.ok(resultJson);
        }
        if ("true".equals(like)) {
            Like userLike = new Like();
            userLike.setUserId(userId);
            userLike.setWyyId(Long.parseLong(id));
            userLike.setCreateTime(new Date());
            QueryWrapper<Music> queryWrapMusic = new QueryWrapper<>();
            queryWrapMusic.eq("WYY_ID", userLike.getWyyId());
            Music music = musicMapper.selectOne(queryWrapMusic);
            userLike.setMusicId(music.getId());
            likeMapper.insert(userLike);
        } else {
            QueryWrapper<Like> queryDelete = new QueryWrapper<>();
            queryDelete.eq("USER_ID", userId);
            queryDelete.eq("WYY_ID", id);
            likeMapper.delete(queryDelete);
        }

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
        return null;
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