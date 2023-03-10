package com.faker.audioStation.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.aop.LogAndPermissions;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.IdDto;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
        cacheService.set(key, resultText, 7, TimeUnit.DAYS);
        return JSONObject.parseObject(resultText);
    }

    @ApiOperation(value = "根据网易云音乐id获取歌曲信息", notes = "根据网易云音乐id获取歌曲信息")
    @PostMapping(value = "getMusicUrl")
    @ResponseBody
    @LogAndPermissions
    public SongUrlRootBean getMusicUrl(@RequestBody IdDto params) {
        String key = SecureUtil.md5(JSONObject.toJSONString(params));
        String value = cacheService.get(key);
        if (null != value) {
            try {
                return JSONObject.parseObject(value, SongUrlRootBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String url = music163Api + "/song/url";
        log.info("网易云音乐api请求地址:" + url);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", params.getId());
        String resultText = HttpUtil.get(url, paramsMap);
        log.info("网易云音乐api返回:" + resultText);
        if (null == resultText) {
            return null;
        }
        SongUrlRootBean songUrlRootBean = JSONObject.parseObject(resultText, SongUrlRootBean.class);
        log.info(songUrlRootBean.toString());
        songUrlRootBean = musicService.downLoadMusic(songUrlRootBean);
        //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
        cacheService.set(key, JSONObject.toJSONString(songUrlRootBean), 7, TimeUnit.DAYS);
        return songUrlRootBean;
    }

    @ApiOperation(value = "根据网易云音乐id获取歌曲信息", notes = "根据网易云音乐id获取歌曲信息")
    @GetMapping(value = "getMusic")
    @ResponseBody
    @LogAndPermissions
    public void getMusic(@ApiParam("音乐文件id") @RequestParam String id, @ApiParam("token") @RequestParam("__token") String token, HttpServletResponse response) {
        log.info("token=" + token);
        Music music = musicService.getById(id);
        if (null == music) {
            ToolsUtil.setStateInfo(response, 404, "根据[" + id + "]未找到音乐信息");
            return;
        }
        File file = new File(music.getPath());
        if (!file.exists()) {
            log.error("音乐文件地址不存在:" + file.getAbsolutePath());
            ToolsUtil.setStateInfo(response, 404, "音乐文件不存在");
            return;
        }
        ToolsUtil.downloadFile(response, file);
        return;
    }

}
