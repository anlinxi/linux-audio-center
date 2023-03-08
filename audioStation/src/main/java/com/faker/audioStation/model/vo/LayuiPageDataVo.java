package com.faker.audioStation.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@ApiModel("layui分页数据返回结构")
public class LayuiPageDataVo {

    @ApiModelProperty(value = "返回编码", example = "0")
    private Integer code = 500;

    @ApiModelProperty("返回信息")
    private String msg = "未知错误";

    @ApiModelProperty(value = "总行数", example = "10")
    private Long count;

    @ApiModelProperty(value = "分页查询返回数据", example = "[]")
    private List data;

    /**
     * 转换mybatis plus iPage 对象为 Layui分页对象
     *
     * @param iPage
     */
    public void putMybatisPlusIPage(IPage iPage) {
        this.count = iPage.getTotal();
        this.code = 0;
        this.msg = "查询完毕";
        this.data = iPage.getRecords();
    }
}
