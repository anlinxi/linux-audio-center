package com.faker.audioStation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.dto.GetMusicPageParamDto;
import com.faker.audioStation.model.dto.LayuiPageSizeDto;
import com.faker.audioStation.model.dto.PageSizeDto;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.MusicService;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api("音乐控制层")
@RequestMapping(value = "music/")
public class MusicController {

    @Autowired
    @ApiModelProperty("音乐文件服务层")
    MusicService musicService;

    @ApiOperation(value = "获取音乐文件的layui参数", notes = "layui表头参数")
    @RequestMapping(value = "getMusicLayuiColVo")
    @ResponseBody
    public Wrapper<List<LayuiColVo>> getMusicLayuiColVo() {
        return musicService.getMusicLayuiColVo();
    }

    @ApiOperation(value = "获取音乐文件的分页数据", notes = "分页查询")
    @RequestMapping(value = "getMusicPage")
    @ResponseBody
    public Wrapper<IPage<Music>> getMusicPage(@RequestBody GetMusicPageParamDto pageSizeDto) {
        return musicService.getMusicPage(pageSizeDto);
    }
}
