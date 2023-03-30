package com.faker.audioStation.strategies.wyyApi;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.wrapper.Wrapper;

/**
 * 网易云api调用策略
 */
public interface WyyApiStrategies {


    /**
     * 获取定义的url
     *
     * @return
     */
    String getUrl();

    /**
     * 获取请求类型 post/get
     *
     * @return
     */
    Method getMethod();

    /**
     * 网易云方法调用入口
     *
     * @param params
     * @return
     */
    JSONObject getWyyApi(WyyApiDto params);

    /**
     * 个性化的策略执行内容
     *
     * @param params
     * @return
     */
    Wrapper<JSONObject> doSomeThing(WyyApiDto params);

    /**
     * 执行java直连网易云方法
     *
     * @param params
     * @return
     */
    JSONObject getWyyHttp(WyyApiDto params) throws Exception;
}
