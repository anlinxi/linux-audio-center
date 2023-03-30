package com.faker.audioStation.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.faker.audioStation.model.domain.Lyric;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.domain.MusicCover;
import com.faker.audioStation.model.domain.Singer;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.IdDto;
import com.faker.audioStation.model.dto.wyy.songDetail.Al;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songDetail.Songs;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.wrapper.Wrapper;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 音乐 服务类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
public interface MusicService extends IService<Music> {


    /**
     * 获取音乐文件的layui参数
     *
     * @return
     */
    Wrapper<List<LayuiColVo>> getMusicLayuiColVo();

    /**
     * 获取音乐文件的分页数据
     *
     * @param pageSizeDto
     * @return
     */
    Wrapper<IPage<Music>> getMusicPage(GetMusicPageParamDto pageSizeDto);


    /**
     * 网易云音乐详情
     *
     * @param ids
     * @return
     */
    SongDetailRootBean songDetail(String[] ids);

    /**
     * 通过网易云id获取歌词信息
     *
     * @param id
     * @param music
     * @return
     */
    Lyric getLyricByWyyId(Long id, Music music);

    /**
     * 通过网易云id获取歌词信息
     *
     * @param param
     * @return
     */
    Wrapper<JSONObject> getLyricByWyyId(IdDto param);

    /**
     * 根据封面文件id获取封面图片
     *
     * @param id
     * @param response
     */
    void getMusicCoverById(String id, HttpServletResponse response);

    /**
     * 扫描本地音乐
     *
     * @return
     */
    Wrapper scanDiskMusic();

    /**
     * 根据网易云id获取mv视频
     *
     * @param id
     * @param response
     */
    void getMvByWyyId(String id, HttpServletResponse response);
}
