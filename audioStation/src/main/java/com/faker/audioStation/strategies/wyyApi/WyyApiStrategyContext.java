package com.faker.audioStation.strategies.wyyApi;

import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.model.dto.WyyApiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 网易云策略Context
 */
@Slf4j
@Component
public class WyyApiStrategyContext {

    /**
     * 环节Map dealNode -> AbstractDealStrategy
     */
    private final List<WyyApiStrategies> detailJsonMap = new ArrayList<>();

    /**
     * 注入所有实现了Strategy接口的Bean
     *
     * @param strategyMap 实现 AbstractDealStrategy 的策略
     */
    @Autowired
    public WyyApiStrategyContext(List<WyyApiStrategies> strategyMap) {
        strategyMap.forEach(item -> {
            this.detailJsonMap.add(item);
        });
    }

    /**
     * 根据网易云请求信息获取策略
     *
     * @param url
     * @param method
     * @return
     */
    public WyyApiStrategies getWyyApiStrategies(String url, String method) {
        if (null != url && null != method) {
            String uri = url;
            if (url.contains("?")) {
                uri = url.substring(0, url.indexOf("?"));
            }
            method = method.toUpperCase();
            log.debug("匹配策略：uri[" + uri + "][" + method + "]");
            for (WyyApiStrategies wyyApiStrategies : detailJsonMap) {
//                log.info("遍历匹配策略：uri[" + wyyApiStrategies.getUrl() + "][" + wyyApiStrategies.getMethod().name() + "]");
                if (wyyApiStrategies.getUrl().equals(uri)
                        && wyyApiStrategies.getMethod().name().toUpperCase().equals(method)) {
                    log.info("匹配到策略：uri[" + uri + "][" + method + "]:" + wyyApiStrategies.getClass().getName());
                    return wyyApiStrategies;
                }
            }
        }
        return null;
    }

    /**
     * 根据网易云请求参数执行策略
     *
     * @param params
     * @return
     */
    public JSONObject doWyyApiStrategies(WyyApiDto params) {
        WyyApiStrategies wyyApiStrategies = this.getWyyApiStrategies(params.getUrl(), params.getMethod());
        if (null != wyyApiStrategies) {
            return wyyApiStrategies.getWyyApi(params);
        }
        return null;
    }
}
