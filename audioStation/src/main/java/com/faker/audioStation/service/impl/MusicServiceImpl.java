package com.faker.audioStation.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.audioStation.enums.PathEnum;
import com.faker.audioStation.mapper.MusicMapper;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songUrl.JsonData;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import java.io.File;
import java.util.Arrays;
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

    @Autowired
    @ApiModelProperty("缓存服务")
    CacheService cacheService;

    @ApiModelProperty("网易云音乐api")
    NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();

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
     * 下载网易云音乐的歌曲到本地
     *
     * @param songUrlRootBean
     * @return
     */
    @Override
    public SongUrlRootBean downLoadMusic(SongUrlRootBean songUrlRootBean) {
        String musicPath = resourcePath + PathEnum.DOWNLOAD_MUSIC_PATH.getPath() + "/";
        if (null != songUrlRootBean && songUrlRootBean.getData().size() > 0) {
            JsonData jsonData = songUrlRootBean.getData().get(0);


            SongDetailRootBean songJson = this.songDetail(new String[]{jsonData.getId() + ""});
            log.info(songJson.toString());
            String url = jsonData.getUrl();
            if (null != url) {
//                HttpUtil.downloadFile(url, new File(musicPath));
            }
        }
        return songUrlRootBean;
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
        songDetailRootBean = JSONObject.parseObject(neteaseCloudMusicInfo.songDetail(parameter).toJSONString(), SongDetailRootBean.class);
        cacheService.set(ids, songDetailRootBean, 30, TimeUnit.DAYS);
        return songDetailRootBean;
    }
}
