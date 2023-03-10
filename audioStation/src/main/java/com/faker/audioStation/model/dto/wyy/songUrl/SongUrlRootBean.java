package com.faker.audioStation.model.dto.wyy.songUrl;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>JsonRootBean</p>
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
 * @date 2023/3/10 20:50
 */
@Data
@NoArgsConstructor
@ApiModel("网易云返回对象")
public class SongUrlRootBean implements Serializable {

    @ApiModelProperty(value = "返回数据")
    private List<JsonData> data;

    @ApiModelProperty(value = "返回编码", example = "200")
    private int code;
}
