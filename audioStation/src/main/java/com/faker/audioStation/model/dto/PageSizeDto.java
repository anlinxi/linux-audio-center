package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("分页查询参数")
public class PageSizeDto {

    @ApiModelProperty(value = "每页记录条数", example = "10")
    private Long pageSize = 10L;

    @ApiModelProperty("页码")
    private Long pageIndex = 0L;

    /**
     * 分页查询参数
     *
     * @return sql start
     */
    public Long getStart() {
        return pageIndex * pageSize;
    }
}
