package com.faker.audioStation.model.dto.wyy.songUrl;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>FreeTimeTrialPrivilege</p>
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
 * @date 2023/3/10 21:01
 */
@Data
@NoArgsConstructor
@ApiModel("网易云返回对象FreeTimeTrialPrivilege")
public class FreeTimeTrialPrivilege implements Serializable {
    private boolean resConsumable;
    private boolean userConsumable;
    private int type;
    private int remainTime;
}
