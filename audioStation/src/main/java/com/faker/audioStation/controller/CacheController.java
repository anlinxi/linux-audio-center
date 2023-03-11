package com.faker.audioStation.controller;

import com.faker.audioStation.aop.LogAndPermissions;
import com.faker.audioStation.model.dto.NameDto;
import com.faker.audioStation.service.CacheService;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>TableController</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/10</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/10 11:25
 */
@Controller
@Api("数据表格控制层")
@RequestMapping(value = "/api/cache/")
public class CacheController {

    @Autowired
    @ApiModelProperty("缓存服务")
    CacheService cacheService;

    @ApiOperation(value = "获取所有缓存key参数", notes = "")
    @PostMapping(value = "allCacheKeys")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper<List<String>> allCacheKeys() {
        return WrapMapper.ok(cacheService.allCacheKeys());
    }

    @ApiOperation(value = "通过键名获取缓存数据", notes = "")
    @PostMapping(value = "getCacheByKeyName")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper getLayuiColVo(@RequestBody NameDto param) {
        return WrapMapper.ok(cacheService.get(param.getName()));
    }

    @ApiOperation(value = "通过键名删除缓存数据", notes = "")
    @PostMapping(value = "removeCacheByKeyName")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper removeCacheByKeyName(@RequestBody NameDto param) {
        cacheService.delete(param.getName());
        return WrapMapper.ok();
    }
}
