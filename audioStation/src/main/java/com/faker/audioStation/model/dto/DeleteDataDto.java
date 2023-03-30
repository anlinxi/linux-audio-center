package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>删除对象参数</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/8</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/8 10:23
 */
@Data
@NoArgsConstructor
@ApiModel("删除对象参数")
public class DeleteDataDto {

    @ApiModelProperty(value = "实体类名字", example = "Music")
    protected String domainName;

    @ApiModelProperty(value = "id", example = "2021122993")
    protected String id;
}
