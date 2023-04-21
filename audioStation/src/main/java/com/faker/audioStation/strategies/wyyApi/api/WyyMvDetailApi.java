package com.faker.audioStation.strategies.wyyApi.api;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.enums.PathEnum;
import com.faker.audioStation.enums.WyyApiTypeEnum;
import com.faker.audioStation.model.domain.Mv;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.strategies.wyyApi.WyyApiAbstract;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * mv策略
 */
@Slf4j
@Component
public class WyyMvDetailApi extends WyyApiAbstract {

    @ApiModelProperty("定义的网易云请求参数")
    protected String url = "/mv/url";

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
        if (null != params.getData().get("id")) {
            String id = ToolsUtil.getString(params.getData().get("id"));
            //分辨率 清晰度
            String resolution = ToolsUtil.getString(params.getData().get("r"));
            Mv mv = mvMapper.selectById(id);
            if (null != mv) {
                File mvFile = new File(mv.getPath());
                if (mvFile.exists()) {
                    JSONObject result = new JSONObject();
                    result.put("code", 200);
                    result.put("id", mv.getWyyId());
                    result.put("mv", mv.getSize());
                    result.put("name", mv.getName());
                    result.put("url", "/api/mv/getMvByWyyId?id=" + mv.getWyyId());
                    WrapMapper.ok(result);
                }

            } else {
                String key = "LOCK:" + this.getClass().getSimpleName() + ":doSomeThing:" + params.getUrl();
                Boolean lock = cacheService.get(key);
                if (null == lock) {
                    try {
                        cacheService.set(key, true, 1, TimeUnit.MINUTES);
                        params.setUrl(url);
                        JSONObject json = super.getHttp(params);
                        Mv mvInsert = new Mv();
                        if (200 == json.getInteger("code")) {
                            mvInsert.setWyyId(json.getJSONObject("data").getLong("id"));
                            mvInsert.setUrl(json.getJSONObject("data").getString("url"));
                            mvInsert.setResolution(resolution);

                            //异步下载
                            new Thread(() -> {
                                String mvPath = resourcePath + PathEnum.MV_PATH.getPath() + "/" + ToolsUtil.getFileName(mvInsert.getWyyId() + ".mp4");
                                try {
                                    //查询专辑详情
                                    WyyApiDto paramsMvDetail = new WyyApiDto();
                                    paramsMvDetail.setUrl("/mv/detail?mvid=" + mvInsert.getWyyId());
                                    paramsMvDetail.setMethod("get");
                                    JSONObject mvDetailJson = super.callWyyAPi(paramsMvDetail);
                                    //艺术家
                                    String artistName = mvDetailJson.getJSONObject("data").getString("artistName");
                                    //名称
                                    String name = mvDetailJson.getJSONObject("data").getString("name");
                                    //介绍
                                    String desc = mvDetailJson.getJSONObject("data").getString("desc");
                                    //发布时间
                                    String publishTime = mvDetailJson.getJSONObject("data").getString("publishTime");
                                    mvInsert.setArtistName(artistName);
                                    mvInsert.setName(name);
                                    mvInsert.setDesc(desc);
                                    mvInsert.setPublishTime(publishTime);
                                    mvPath = resourcePath + PathEnum.MV_PATH.getPath() + "/" + ToolsUtil.getFileName(artistName + " - " + name + ".mp4");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //下载mv
                                File mvFile = new File(mvPath);
                                if (!mvFile.getParentFile().exists()) {
                                    mvFile.getParentFile().mkdirs();
                                }
                                mvInsert.setPath(mvPath);
                                HttpUtil.downloadFile(mvInsert.getUrl(), mvFile);
                                //mv大小
                                mvInsert.setSize(new File(mvFile.getAbsolutePath()).length());


                                mvMapper.insert(mvInsert);
                                log.warn("MV视频[" + mvInsert.getWyyId() + "]下载完毕!");
                            }).start();

                            return WrapMapper.ok(json);
                        }
                    } finally {
                        cacheService.delete(key);
                    }
                }
            }
        }
        return WrapMapper.error("查询参数id不能为空");
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
        form.put("id", id);
        if (null != params.getData().get("r")) {
            form.put("r", params.getData().get("r"));
        } else {
            form.put("r", 1080);
        }

        form.put("csrf_token", "");
        String result = wyyHttpUtil.httpContent(WyyApiTypeEnum.WE_API, Method.POST, PROTOCOL + "music.163.com/weapi/song/enhance/play/mv/url", form);
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
        params.setUrl(url);
        params.setMethod(method.name());
        params.getData().put("id", "14620291");
        params.getData().put("r", 1080);
        Wrapper wrapper = this.runTest(params);
        log.info("测试结果:" + wrapper);

//        super.music163Api = "http://127.0.0.1:3000";
//        JSONObject jsonObject = this.callWyyAPi(params);
//        log.info("测试结果:" + jsonObject);
    }
}
