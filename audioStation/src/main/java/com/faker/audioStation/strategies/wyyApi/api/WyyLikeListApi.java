package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.mapper.LikeMapper;
import com.faker.audioStation.model.domain.Like;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户收藏列表
 */
@Slf4j
@Component
public class WyyLikeListApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/likelist";

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

        String userId = params.getUserId();
        QueryWrapper<Like> queryList = new QueryWrapper<>();
        queryList.eq("USER_ID", userId);
        List<Like> likeList = likeMapper.selectList(queryList);
        resultJson.put("likeList", likeList);
        List<Long> ids = new ArrayList<>();
        //核心是生成ids
        likeList.stream().forEach(like -> ids.add(like.getWyyId()));
        resultJson.put("ids", ids);
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
