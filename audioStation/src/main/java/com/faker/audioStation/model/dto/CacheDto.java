package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>CacheDto</p>
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
@ApiModel("缓存对象")
public class CacheDto implements Serializable {

    @ApiModelProperty("缓存键名")
    private String key;

    @ApiModelProperty("缓存值")
    private Object value;

    @ApiModelProperty("缓存值类型")
    private Class classz;

    @ApiModelProperty("缓存创建时间")
    private long initTime;

    @ApiModelProperty("过期时间")
    private long overTime;

    @ApiModelProperty("过期时间单位")
    private TimeUnit timeUnit;

}
