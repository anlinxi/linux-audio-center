package com.faker.audioStation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.audioStation.mapper.JsMobileUserMapper;
import com.faker.audioStation.mapper.MusicMapper;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 音乐 服务实现类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

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
}
