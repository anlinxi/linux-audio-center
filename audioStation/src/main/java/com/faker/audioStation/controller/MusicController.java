package com.faker.audioStation.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.aop.LogAndPermissions;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.IdsDto;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@Api("音乐控制层")
@RequestMapping(value = "/api/music/")
public class MusicController {

    @Value("${faker.music163Api:http://yumbo.top:3000}")
    @ApiModelProperty("网易云音乐API地址")
    private String music163Api;

    @Autowired
    @ApiModelProperty("音乐文件服务层")
    MusicService musicService;

    @Autowired
    @ApiModelProperty("缓存服务")
    CacheService cacheService;

    @ApiOperation(value = "获取音乐文件的layui参数", notes = "layui表头参数")
    @PostMapping(value = "getMusicLayuiColVo")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper<List<LayuiColVo>> getMusicLayuiColVo() {
        return musicService.getMusicLayuiColVo();
    }

    @ApiOperation(value = "获取音乐文件的分页数据", notes = "分页查询")
    @PostMapping(value = "getMusicPage")
    @ResponseBody
    @LogAndPermissions
    public Wrapper<IPage<Music>> getMusicPage(@RequestBody GetMusicPageParamDto pageSizeDto) {
        return musicService.getMusicPage(pageSizeDto);
    }

    @ApiOperation(value = "反向代理网易云音乐api", notes = "网易云音乐api")
    @PostMapping(value = "getWyyApi")
    @ResponseBody
    @LogAndPermissions
    public JSONObject getWyyApi(@RequestBody WyyApiDto params) {
        String key = SecureUtil.md5(JSONObject.toJSONString(params));
        String value = cacheService.get(key);
        if (null != value) {
            try {
                return JSONObject.parseObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String method = params.getMethod().toUpperCase();
        String resultText = null;
        String url = music163Api + params.getUrl();
        log.info("网易云音乐api请求地址:" + url);
        if ("GET".equals(method)) {
            resultText = HttpUtil.get(url, params.getData());
        } else if ("POST".equals(method)) {
            resultText = HttpUtil.get(url, params.getData());
        }
        log.info("网易云音乐api返回:" + resultText);
        if (null == resultText) {
            return null;
        }
        //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
        cacheService.set(key, resultText, 10, TimeUnit.HOURS);
        return JSONObject.parseObject(resultText);
    }

    @ApiOperation(value = "根据网易云音乐id获取歌曲信息", notes = "根据网易云音乐id获取歌曲信息")
    @PostMapping(value = "getMusicUrl")
    @ResponseBody
    @LogAndPermissions
    public JSONObject getMusicUrl(@RequestBody IdsDto params) {
        String key = SecureUtil.md5(JSONObject.toJSONString(params));
        String value = cacheService.get(key);
        if (null != value) {
            try {
                return JSONObject.parseObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String url = music163Api + "/song/url";
        log.info("网易云音乐api请求地址:" + url);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("ids", params.getIds());
        String resultText = HttpUtil.get(url, paramsMap);
        log.info("网易云音乐api返回:" + resultText);
        if (null == resultText) {
            return null;
        }
        //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
        cacheService.set(key, resultText, 24, TimeUnit.HOURS);
        return JSONObject.parseObject(resultText);
    }

}
