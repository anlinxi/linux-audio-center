package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>IdsDto</p>
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
 * @date 2023/3/10 16:19
 */
@Data
@NoArgsConstructor
@ApiModel(value = "多个id查询参数",description = "用,分割的多个id")
public class IdsDto {

    @ApiModelProperty(value = "多个id", example = "2021122993,2021122994")
    protected String ids;
}
