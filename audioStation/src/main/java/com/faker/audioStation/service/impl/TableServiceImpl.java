package com.faker.audioStation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faker.audioStation.mapper.*;
import com.faker.audioStation.model.domain.*;
import com.faker.audioStation.model.dto.GetPageDto;
import com.faker.audioStation.service.TableService;
import com.faker.audioStation.util.ToolsUtil;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐 服务实现类
 * </p>
 *
 * @author anlin
 * @since 2022-07-26
 */
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    @ApiModelProperty("音乐文件Mapper")
    MusicMapper musicMapper;

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
    MvMapper mvMapper;

    /**
     * 获取实体类文件的分页数据
     *
     * @param pageSizeDto
     * @return
     */
    @Override
    public IPage<?> getPage(GetPageDto pageSizeDto) {
        IPage<?> musicIPage = null;
        if ("Music".equals(pageSizeDto.getDomainName())) {
            Page<Music> pageParam = new Page<>(pageSizeDto.getPage(), pageSizeDto.getPageSize());
            QueryWrapper<Music> musicQueryWrapper = new QueryWrapper<>();
            if ("asc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Music.class, pageSizeDto.getField());
                musicQueryWrapper.orderByAsc(tableField);
            }
            if ("desc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Music.class, pageSizeDto.getField());
                musicQueryWrapper.orderByDesc(tableField);
            }
            musicIPage = musicMapper.selectPage(pageParam, musicQueryWrapper);
        } else if ("MusicCover".equals(pageSizeDto.getDomainName())) {
            Page<MusicCover> pageParam = new Page<>(pageSizeDto.getPage(), pageSizeDto.getPageSize());
            QueryWrapper<MusicCover> queryWrapper = new QueryWrapper<>();
            if ("asc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(MusicCover.class, pageSizeDto.getField());
                queryWrapper.orderByAsc(tableField);
            }
            if ("desc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(MusicCover.class, pageSizeDto.getField());
                queryWrapper.orderByDesc(tableField);
            }
            musicIPage = musicCoverMapper.selectPage(pageParam, queryWrapper);
        } else if ("Lyric".equals(pageSizeDto.getDomainName())) {
            Page<Lyric> pageParam = new Page<>(pageSizeDto.getPage(), pageSizeDto.getPageSize());
            QueryWrapper<Lyric> queryWrapper = new QueryWrapper<>();
            if ("asc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Lyric.class, pageSizeDto.getField());
                queryWrapper.orderByAsc(tableField);
            }
            if ("desc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Lyric.class, pageSizeDto.getField());
                queryWrapper.orderByDesc(tableField);
            }
            musicIPage = lyricMapper.selectPage(pageParam, queryWrapper);
        } else if ("Singer".equals(pageSizeDto.getDomainName())) {
            Page<Singer> pageParam = new Page<>(pageSizeDto.getPage(), pageSizeDto.getPageSize());
            QueryWrapper<Singer> queryWrapper = new QueryWrapper<>();
            if ("asc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Singer.class, pageSizeDto.getField());
                queryWrapper.orderByAsc(tableField);
            }
            if ("desc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Singer.class, pageSizeDto.getField());
                queryWrapper.orderByDesc(tableField);
            }
            musicIPage = singerMapper.selectPage(pageParam, queryWrapper);
        } else if ("Mv".equals(pageSizeDto.getDomainName())) {
            Page<Mv> pageParam = new Page<>(pageSizeDto.getPage(), pageSizeDto.getPageSize());
            QueryWrapper<Mv> queryWrapper = new QueryWrapper<>();
            if ("asc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Singer.class, pageSizeDto.getField());
                queryWrapper.orderByAsc(tableField);
            }
            if ("desc".equals(pageSizeDto.getOrder())) {
                String tableField = ToolsUtil.getTableField(Singer.class, pageSizeDto.getField());
                queryWrapper.orderByDesc(tableField);
            }
            musicIPage = mvMapper.selectPage(pageParam, queryWrapper);
        } else if ("xxx".equals(pageSizeDto.getDomainName())) {

        } else {
            return null;
        }
        return musicIPage;
    }
}
