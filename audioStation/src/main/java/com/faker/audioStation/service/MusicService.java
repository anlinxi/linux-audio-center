package com.faker.audioStation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
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
}
