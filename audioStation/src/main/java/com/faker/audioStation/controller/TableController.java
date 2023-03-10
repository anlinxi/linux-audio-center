package com.faker.audioStation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.faker.audioStation.aop.LogAndPermissions;
import com.faker.audioStation.conf.SqliteInit;
import com.faker.audioStation.model.dto.GetPageDto;
import com.faker.audioStation.model.dto.NameDto;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.service.TableService;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping(value = "/api/table/")
public class TableController {

    @Autowired
    @ApiModelProperty("表格服务层")
    TableService tableService;

    @ApiOperation(value = "获取所有mybatis plus对象的参数", notes = "layui表头参数")
    @RequestMapping(value = "getAllDomainList")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper<Map<String, String>> getAllDomainList() {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, Class> entry : SqliteInit.classMap.entrySet()) {


            //判断是否有指定主解
            Annotation anno = entry.getValue().getAnnotation(ApiModel.class);
            if (anno != null && anno instanceof ApiModel) {
                //将注解中的类型值作为key，对应的类作为 value
                ApiModel apiModel = (ApiModel) anno;
                map.put(entry.getKey(), apiModel.value());
            } else {
                map.put(entry.getKey(), entry.getValue().getSimpleName());
            }
        }
        return WrapMapper.ok(map);
    }

    @ApiOperation(value = "获取mybatis plus实体类参数", notes = "layui表头参数")
    @RequestMapping(value = "getLayuiColVo")
    @ResponseBody
    @LogAndPermissions("1")
    public Wrapper<List<LayuiColVo>> getLayuiColVo(@RequestBody NameDto param) {
        Class clazz = SqliteInit.classMap.get(param.getName());
        if (null == clazz) {
            return WrapMapper.error("未查询到对应的实体类[" + param.getName() + "]");
        }
        List<LayuiColVo> layuiColVoList = ToolsUtil.getApiModelProperty(clazz);
        return WrapMapper.ok(layuiColVoList);
    }

    @ApiOperation(value = "获取实体类文件的分页数据", notes = "分页查询")
    @RequestMapping(value = "getPage")
    @ResponseBody
    @LogAndPermissions
    public Wrapper<IPage<?>> getPage(@RequestBody GetPageDto pageSizeDto) {
        Class clazz = SqliteInit.classMap.get(pageSizeDto.getDomainName());
        if (null == clazz) {
            return WrapMapper.error("未查询到对应的实体类[" + pageSizeDto.getDomainName() + "]");
        }
        IPage<?> iPage = tableService.getPage(pageSizeDto);
        return WrapMapper.ok(iPage);
    }
}
