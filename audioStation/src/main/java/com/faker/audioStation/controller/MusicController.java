package com.faker.audioStation.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.aop.LogAndPermissions;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.IdDto;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.strategies.wyyApi.WyyApiStrategyContext;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
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
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
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

    @Autowired
    @ApiModelProperty("网易云api策略")
    WyyApiStrategyContext wyyApiStrategyContext;

    @ApiModelProperty("网易云音乐api")
    NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();

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
        //如果有自定义策略 优先执行策略内容
        JSONObject jsonObject = wyyApiStrategyContext.doWyyApiStrategies(params);
        if (null != jsonObject) {
            return jsonObject;
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
        if (null == resultText) {
            return null;
        }
        if (resultText.length() < 100) {
            log.info("网易云音乐api返回:" + resultText);
        }
        //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
        cacheService.set(key, resultText, 8, TimeUnit.HOURS);
        return JSONObject.parseObject(resultText);
    }

    @ApiOperation(value = "根据网易云音乐id获取歌曲信息", notes = "根据网易云音乐id获取歌曲信息")
    @PostMapping(value = "getMusicUrl")
    @ResponseBody
    @LogAndPermissions
    public SongUrlRootBean getMusicUrl(@RequestBody IdDto params) {
        String key = "getMusicUrl:" + SecureUtil.md5(JSONObject.toJSONString(params));
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
        if (null == resultText) {
            return null;
        }
        if (resultText.length() < 100) {
            log.info("网易云音乐api返回:" + resultText);
        }
        SongUrlRootBean songUrlRootBean = JSONObject.parseObject(resultText, SongUrlRootBean.class);
        log.info(songUrlRootBean.toString());
        new Thread(() -> {
            SongUrlRootBean songUrlRootBeanV2 = musicService.downLoadMusic(songUrlRootBean);
            //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
            cacheService.set(key, JSONObject.toJSONString(songUrlRootBeanV2), 7, TimeUnit.DAYS);
        }).start();
        return songUrlRootBean;
    }


    @ApiOperation(value = "根据网易云音乐id获取歌曲信息", notes = "根据网易云音乐id获取歌曲信息")
    @PostMapping(value = "songDetail")
    @ResponseBody
    @LogAndPermissions
    public SongDetailRootBean songDetail(@RequestBody IdDto params) {
        String key = "songDetail:" + SecureUtil.md5(JSONObject.toJSONString(params));
        String value = cacheService.get(key);
        if (null != value) {
            try {
                return JSONObject.parseObject(value, SongDetailRootBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String url = music163Api + "/song/url";
        log.info("网易云音乐api请求地址:" + url);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", params.getId());
        String resultText = HttpUtil.get(url, paramsMap);
        if (null == resultText) {
            return null;
        }
        if (resultText.length() < 100) {
            log.info("网易云音乐api返回:" + resultText);
        }
        SongUrlRootBean songUrlRootBean = JSONObject.parseObject(resultText, SongUrlRootBean.class);
        //下载音乐
        musicService.downLoadMusic(songUrlRootBean);
        //返回对象
        SongDetailRootBean songJson = musicService.songDetail(new String[]{params.getId() + ""});
        //减小网易云音乐api鸭梨 缓存一些信息，免得频繁调用api被封
        cacheService.set(key, JSONObject.toJSONString(songJson), 7, TimeUnit.DAYS);
        return songJson;
    }

    @ApiOperation(value = "根据网易云音乐id获取歌曲信息", notes = "根据网易云音乐id获取歌曲信息")
    @GetMapping(value = "getMusic")
    @ResponseBody
    @LogAndPermissions
    public void getMusic(@ApiParam("音乐文件id") @RequestParam String id, @ApiParam("token") @RequestParam("__token") String token, HttpServletResponse response) {
        log.info("token=" + token);
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ID", id).or().eq("WYY_ID", id);
        Music music = musicService.getOne(queryWrapper);
        if (null == music) {
            String url = music163Api + "/song/url";
            log.info("网易云音乐api请求地址:" + url);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("id", id);
            String resultText = HttpUtil.get(url, paramsMap);
            if (null == resultText) {
                ToolsUtil.setStateInfo(response, 404, "根据[" + id + "]未找到音乐信息");
                return;
            }
            if (resultText.length() < 100) {
                log.info("网易云音乐api返回:" + resultText);
            }
            SongUrlRootBean songUrlRootBean = JSONObject.parseObject(resultText, SongUrlRootBean.class);
            log.info(songUrlRootBean.toString());
            songUrlRootBean = musicService.downLoadMusic(songUrlRootBean);
            QueryWrapper<Music> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("WYY_ID", id);
            music = musicService.getOne(queryWrapper2);
            if (null == music) {
                ToolsUtil.setStateInfo(response, 404, "根据[" + id + "]未找到音乐信息");
                return;
            }
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

    @ApiOperation(value = "通过网易云id获取歌词信息", notes = "通过网易云id获取歌词")
    @PostMapping(value = "getLyricByWyyId")
    @ResponseBody
    @LogAndPermissions
    public Wrapper<JSONObject> getLyricByWyyId(@RequestBody IdDto params) {
        String key = SecureUtil.md5("getLyricByWyyId:" + JSONObject.toJSONString(params));
        JSONObject value = cacheService.get(key);
        if (null != value) {
            return WrapMapper.ok(value);
        }
        Wrapper<JSONObject> wrapper = musicService.getLyricByWyyId(params);
        if (wrapper.success()) {
            cacheService.set(key, wrapper.getResult());
        }
        return wrapper;
    }

    @ApiOperation(value = "获取本地的所有音乐", notes = "")
    @PostMapping(value = "getAllMusic")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper<List<Music>> getAllMusic() {
        String key = "getAllMusic";
        ArrayList<Music> value = cacheService.get(key);
        if (null != value) {
            return WrapMapper.ok(value);
        }
        List<Music> list = musicService.list();
        ArrayList<Music> arrayList = new ArrayList<Music>();
        for (Music music : list) {
            music.setPath(null);
            music.setHashCode(null);
            arrayList.add(music);
        }
        list = null;
        cacheService.set(key, arrayList, 10, TimeUnit.MINUTES);
        return WrapMapper.ok(arrayList);
    }

    @ApiOperation(value = "根据封面文件id获取封面图片", notes = "根据网易云音乐id获取歌曲信息")
    @GetMapping(value = "getMusicCoverById")
    @ResponseBody
    @LogAndPermissions
    public void getMusicCoverById(@ApiParam("封面文件id") @RequestParam String id, HttpServletResponse response) {
        musicService.getMusicCoverById(id, response);
    }

    @ApiOperation(value = "扫描本地音乐", notes = "")
    @PostMapping(value = "scanDiskMusic")
    @ResponseBody
    @LogAndPermissions
    public Wrapper scanDiskMusic() {
        return musicService.scanDiskMusic();
    }
}
