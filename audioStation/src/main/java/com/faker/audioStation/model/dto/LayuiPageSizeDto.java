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
    private Long limit = 10L;

    @ApiModelProperty("页码")
    private Long page = 1L;

    public long getPageIndex() {
        return page;
    }

    public long getPageSize() {
        return limit;
    }
}
