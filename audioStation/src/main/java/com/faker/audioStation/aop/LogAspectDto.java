package com.faker.audioStation.aop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>日志权限切面参数</p>
 *
 * <p>项目名称：lnwlcsMicroService</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2022/12/30</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2022/12/30 10:04
 */
@Data
@NoArgsConstructor
@ApiModel(value = "日志权限切面参数")
public class LogAspectDto {

    @ApiModelProperty("进入切面的开始时间")
    private Date startTime;

    @ApiModelProperty("离开切面的开始时间")
    private Date endTime;

    /**
     * 获取耗时
     *
     * @return
     */
    public Long getUsedTime() {
        if (startTime == null) {
            return null;
        }
        if (endTime == null) {
            endTime = new Date();
        }
        return endTime.getTime() - startTime.getTime();
    }
}
