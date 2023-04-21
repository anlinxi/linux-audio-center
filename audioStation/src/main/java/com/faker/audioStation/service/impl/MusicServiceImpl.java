package com.faker.audioStation.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.audioStation.enums.PathEnum;
import com.faker.audioStation.mapper.*;
import com.faker.audioStation.model.domain.*;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.IdDto;
import com.faker.audioStation.model.dto.WyyApiDto;
import com.faker.audioStation.model.dto.wyy.songDetail.Al;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songDetail.Songs;
import com.faker.audioStation.model.dto.wyy.songUrl.JsonData;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.strategies.wyyApi.WyyApiStrategies;
import com.faker.audioStation.strategies.wyyApi.WyyApiStrategyContext;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.util.WyyHttpUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 音乐 服务实现类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
@Slf4j
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

    @Value("${faker.resources:/music/}")
    @ApiModelProperty("资源文件路径")
    private String resourcePath;

    @Value("${faker.unblockNeteaseMusic.proxy:}")
    @ApiModelProperty("解锁网易云灰色音乐的代理")
    private String unblockNeteaseMusicProxy;

    @Autowired
    @ApiModelProperty("缓存服务")
    CacheService cacheService;

    @ApiModelProperty("网易云音乐api")
    NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();

    @Autowired
    @ApiModelProperty("扫描器")
    Scanner scanner;

    @Autowired
    @ApiModelProperty("音乐封面图片文件Mapper")
    MusicCoverMapper musicCoverMapper;

    @Autowired
    @ApiModelProperty("歌手Mapper")
    SingerMapper singerMapper;

    @Autowired
    @ApiModelProperty("歌词Mapper")
    LyricMapper lyricMapper;

    @Autowired
    @ApiModelProperty("Mv信息mapper")
    protected MvMapper mvMapper;

    @Autowired
    @ApiModelProperty("网易云api策略")
    WyyApiStrategyContext wyyApiStrategyContext;

    @ApiModelProperty("java的网易云音乐直连api")
    protected WyyHttpUtil wyyHttpUtil;

    @ApiModelProperty(value = "歌曲封面图片类型", notes = "bmp|gif|jpg|jpeg|png")
    private String formatName = "png";


    /**
     * 获取音乐文件的layui参数
     *
     * @return
     */
    @Override
    public Wrapper<List<LayuiColVo>> getMusicLayuiColVo() {
        List<LayuiColVo> layuiColVoList = ToolsUtil.getApiModelProperty(Music.class);
        return WrapMapper.ok(layuiColVoList);
    }

    /**
     * 获取音乐文件的分页数据
     *
     * @param pageSizeDto
     * @return
     */
    @Override
    public Wrapper<IPage<Music>> getMusicPage(GetMusicPageParamDto pageSizeDto) {
        Page<Music> pageParam = new Page<>(pageSizeDto.getPage(), pageSizeDto.getPageSize());
        QueryWrapper<Music> musicQueryWrapper = new QueryWrapper<>();
        musicQueryWrapper.like(ToolsUtil.isNotNull(pageSizeDto.getName()), "TITLE", pageSizeDto.getName());
        if ("asc".equals(pageSizeDto.getOrder())) {
            String tableField = ToolsUtil.getTableField(Music.class, pageSizeDto.getField());
            musicQueryWrapper.orderByAsc(tableField);
        }
        if ("desc".equals(pageSizeDto.getOrder())) {
            String tableField = ToolsUtil.getTableField(Music.class, pageSizeDto.getField());
            musicQueryWrapper.orderByDesc(tableField);
        }


        IPage<Music> musicIPage = this.page(pageParam, musicQueryWrapper);
        return WrapMapper.ok(musicIPage);
    }

    /**
     * 网易云音乐详情
     *
     * @param idsArr
     * @return
     */
    @Override
    public SongDetailRootBean songDetail(String[] idsArr) {
        String ids = Arrays.stream(idsArr).collect(Collectors.joining(","));
        SongDetailRootBean songDetailRootBean = cacheService.get(ids);
        if (songDetailRootBean != null) {
            return songDetailRootBean;
        }
        JSONObject parameter = new JSONObject();
        parameter.put("ids", ids);
        String songJson = neteaseCloudMusicInfo.songDetail(parameter).toJSONString();
        log.info(songJson);
        songDetailRootBean = JSONObject.parseObject(songJson, SongDetailRootBean.class);
        cacheService.set(ids, songDetailRootBean, 30, TimeUnit.DAYS);
        return songDetailRootBean;
    }

    /**
     * 通过网易云id获取歌词信息
     *
     * @param id
     * @param music
     * @return
     */
    @Override
    public Lyric getLyricByWyyId(Long id, Music music) {
        QueryWrapper<Lyric> query = new QueryWrapper<>();
        query.eq("WYY_ID", id);
        Lyric lyric = lyricMapper.selectOne(query);
        if (null == lyric) {
            try {
                JSONObject searchlyric = new JSONObject();
                searchlyric.put("id", id + "");
                JSONObject searchlyricResult = neteaseCloudMusicInfo.lyric(searchlyric);
                String lyricText = searchlyricResult.getJSONObject("lrc").getString("lyric");
                String lyricPath = resourcePath + PathEnum.LYRIC_PATH.getPath() + "/" + ToolsUtil.getFileName(music) + ".lrc";
                File lyricPathFile = new File(lyricPath);
                if (!lyricPathFile.getParentFile().exists()) {
                    lyricPathFile.getParentFile().mkdirs();
                }
                FileWriter writer = new FileWriter(lyricPathFile);
                writer.write(lyricText);
                writer.flush();
                writer.close();
                lyric = new Lyric();
                lyric.setId(music.getId());
                lyric.setPath(lyricPath);
                lyric.setName(music.getTitle());
                lyric.setWyyId(searchlyricResult.getJSONObject("lrc").getLong("id"));
                lyricMapper.insert(lyric);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lyric;
    }

    /**
     * 通过网易云id获取歌词信息
     *
     * @param param
     * @return
     */
    @Override
    public Wrapper<JSONObject> getLyricByWyyId(IdDto param) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("WYY_ID", param.getId()).or().eq("ID", param.getId());
        Music music = this.getOne(queryWrapper);
        if (null == music) {
            JSONObject searchlyric = new JSONObject();
            searchlyric.put("id", param.getId() + "");
            JSONObject searchlyricResult = neteaseCloudMusicInfo.lyric(searchlyric);
            return WrapMapper.ok(searchlyricResult);
        }
        if (param.getId().equals(music.getId() + "")) {
            param.setId(music.getWyyId() + "");
        }
        Lyric lyric = this.getLyricByWyyId(Long.parseLong(param.getId()), music);
        cn.hutool.core.io.file.FileReader reader = new cn.hutool.core.io.file.FileReader(lyric.getPath());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        JSONObject lrc = new JSONObject();
        lrc.put("lyric", reader.readString());
        jsonObject.put("lrc", lrc);
        return WrapMapper.ok(jsonObject);
    }

    /**
     * 根据封面文件id获取封面图片
     *
     * @param id
     * @param response
     */
    @Override
    public void getMusicCoverById(String id, HttpServletResponse response) {
        MusicCover musicCover = musicCoverMapper.selectById(id);
        if (null == musicCover) {
            ToolsUtil.setStateInfo(response, 404, "根据[" + id + "]未找到音乐封面信息");
            return;
        }
        File file = new File(musicCover.getPath());
        if (!file.exists()) {
            log.error("封面图片文件地址不存在:" + file.getAbsolutePath());
            ToolsUtil.setStateInfo(response, 404, "封面图片文件不存在");
            return;
        }
        ToolsUtil.downloadFile(response, file);
    }

    /**
     * 扫描本地音乐
     *
     * @return
     */
    @Override
    public Wrapper scanDiskMusic() {
        return scanner.startScan(resourcePath);
    }

    /**
     * 根据网易云id获取mv视频
     *
     * @param id
     * @param response
     */
    @Override
    public void getMvByWyyId(String id, HttpServletResponse response) {
        Mv mv = mvMapper.selectById(id);
        if (null == mv) {
            WyyApiDto params = new WyyApiDto();
            params.setUrl("/mv/url?id=" + id + "&r=1080");
            params.setMethod("get");
            params.getData().put("id", id);
            params.getData().put("r", 1080);
            WyyApiStrategies wyyApiStrategies = wyyApiStrategyContext.getWyyApiStrategies(params.getUrl(), params.getMethod());
            Wrapper<JSONObject> jsonObjectWrapper = wyyApiStrategies.doSomeThing(params);
            if (jsonObjectWrapper.success()) {
                mv = mvMapper.selectById(jsonObjectWrapper.getResult().getLong("id"));
            }
        }
        if (null == mv) {
            ToolsUtil.setStateInfo(response, 404, "根据[" + id + "]未找到mv信息");
            return;
        }
        File file = new File(mv.getPath());
        if (!file.exists()) {
            log.error("mv视频文件地址不存在:" + file.getAbsolutePath());
            ToolsUtil.setStateInfo(response, 404, "mv视频文件地址不存在");
            return;
        }
        ToolsUtil.downloadFile(response, file);
    }
}
