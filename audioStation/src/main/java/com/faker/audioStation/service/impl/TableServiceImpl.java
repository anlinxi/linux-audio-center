package com.faker.audioStation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faker.audioStation.conf.SqliteInit;
import com.faker.audioStation.mapper.*;
import com.faker.audioStation.model.domain.*;
import com.faker.audioStation.model.dto.DeleteDataDto;
import com.faker.audioStation.model.dto.GetPageDto;
import com.faker.audioStation.service.TableService;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
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

    /**
     * 删除一条数据
     *
     * @param param
     * @return
     */
    @Override
    public Wrapper delete(DeleteDataDto param) {
        if (ToolsUtil.isNullOrEmpty(param.getId())) {
            return WrapMapper.error("删除id不能为空!");
        }
        Class clazz = SqliteInit.classMap.get(param.getDomainName());
        if (null == clazz) {
            return WrapMapper.error("未查询到对应的实体类[" + param.getDomainName() + "]");
        }
        int musicIPage = 0;
        if ("Music".equals(param.getDomainName())) {
            musicIPage = musicMapper.deleteById(param.getId());
        } else if ("MusicCover".equals(param.getDomainName())) {
            musicIPage = musicCoverMapper.deleteById(param.getId());
        } else if ("Lyric".equals(param.getDomainName())) {
            musicIPage = lyricMapper.deleteById(param.getId());
        } else if ("Singer".equals(param.getDomainName())) {
            musicIPage = singerMapper.deleteById(param.getId());
        } else if ("Mv".equals(param.getDomainName())) {
            musicIPage = mvMapper.deleteById(param.getId());
        } else {
            return WrapMapper.error("未定义的类型[" + param.getDomainName() + "]");
        }
        return WrapMapper.ok(musicIPage);
    }
}
