package com.faker.audioStation.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>WyyApiDto</p>
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
 * @date 2023/3/10 14:06
 */
@Data
@NoArgsConstructor
@ApiModel("网易云api查询参数")
public class WyyApiDto {

    @ApiModelProperty(value = "接口方法", example = "GET")
    private String method;

    @ApiModelProperty(value = "相对地址", example = "/personalized/newsong")
    private String url;

    @ApiModelProperty(value = "用户id", notes = "根据请求头自动set")
    private String userId;

    @ApiModelProperty(value = "参数")
    private Map<String, Object> data = new HashMap<>();
}
