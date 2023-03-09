package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>SqliteTableStructureDto</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/9</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/9 9:33
 */
@Data
@NoArgsConstructor
@ApiModel("sqlite表结构字段")
public class SqliteTableStructureDto {

    @ApiModelProperty("主键id")
    private String cid;

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段映射")
    private String type;

    @ApiModelProperty("是否允许为空")
    private boolean notNull;

    @ApiModelProperty("默认值")
    private String dfltValue;

    @ApiModelProperty("是否为主键")
    private boolean pk;

}
