package com.faker.audioStation.test;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>GetMusicDetailTest</p>
 *
 * <p>项目名称：linux-audio-center</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/28</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/28 21:42
 */
@Slf4j
public class GetMusicDetailTest {

    @ApiModelProperty("网易云音乐API地址")
    protected String music163Api = "http://127.0.0.1:3000";

    @Test
    public void test1() throws Exception {
        String id = "2029137267";
        id = "29850683";
        String url2 = music163Api + "/song/detail?proxy=http://192.168.123.223:33335";
        Map<String, Object> paramsMap2 = new HashMap<>();
        paramsMap2.put("ids", id);
        Proxy proxy = null;
//        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.123.224", 33335));
        HttpResponse response = HttpUtil.createPost(url2).form(paramsMap2).setProxy(proxy).executeAsync();
        String searchText = response.body();
        log.info(searchText);
//        JSONObject json = JSONObject.parseObject(searchText);
//        log.info(json.toJSONString());
    }
}
