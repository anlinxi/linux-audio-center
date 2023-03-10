package com.faker.audioStation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.wyy.songDetail.SongDetailRootBean;
import com.faker.audioStation.model.dto.wyy.songUrl.SongUrlRootBean;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.wrapper.Wrapper;

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
     * 下载网易云音乐的歌曲到本地
     *
     * @param songUrlRootBean
     * @return
     */
    SongUrlRootBean downLoadMusic(SongUrlRootBean songUrlRootBean);

    /**
     * 网易云音乐详情
     *
     * @param ids
     * @return
     */
    SongDetailRootBean songDetail(String[] ids);
}
