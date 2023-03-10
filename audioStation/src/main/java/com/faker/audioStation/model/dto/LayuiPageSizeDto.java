package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("layui分页查询参数")
public class LayuiPageSizeDto {

    @ApiModelProperty(value = "每页记录条数", example = "10")
    protected Long limit = 10L;

    @ApiModelProperty("页码")
    protected Long page = 1L;

    @ApiModelProperty(value = "排序字段", example = "")
    protected String field;

    @ApiModelProperty(value = "排序方式", example = "desc")
    protected String order;

    public long getPageIndex() {
        return page;
    }

    public long getPageSize() {
        return limit;
    }
}
