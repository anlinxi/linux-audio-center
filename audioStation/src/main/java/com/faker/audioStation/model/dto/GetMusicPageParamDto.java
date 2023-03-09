package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("查询音乐的分页参数")
public class GetMusicPageParamDto extends LayuiPageSizeDto {

    @ApiModelProperty("音乐名称")
    private String name;
}
