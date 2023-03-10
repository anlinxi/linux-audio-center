package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>getPageDto</p>
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
 * @date 2023/3/10 12:01
 */
@Data
@NoArgsConstructor
@ApiModel("layui对象分页查询参数")
public class GetPageDto extends LayuiPageSizeDto {

    @ApiModelProperty(value = "实体类名字", example = "Music")
    protected String domainName;
}
