/**
 * Copyright 2023 bejson.com
 */
package com.faker.audioStation.model.dto.wyy.songDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2023-03-10 21:38:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@NoArgsConstructor
@ApiModel("网易云歌曲专辑信息")
public class Al implements Serializable {

    @ApiModelProperty(value = "专辑封面图片")
    private String picUrl;

    @ApiModelProperty(value = "专辑名称")
    private String name;

    private List<String> tns;

    private String pic_str;

    @ApiModelProperty(value = "专辑id")
    private Long id;

    @ApiModelProperty(value = "封面图片id")
    private Long pic;

}