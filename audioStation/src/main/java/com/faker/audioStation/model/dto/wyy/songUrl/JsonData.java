package com.faker.audioStation.model.dto.wyy.songUrl;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>Data</p>
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
 * @date 2023/3/10 20:56
 */
@Data
@NoArgsConstructor
@ApiModel("网易云返回对象JsonData")
public class JsonData implements Serializable {

    @ApiModelProperty(value = "id")
    private long id;

    @ApiModelProperty(value = "url地址")
    private String url;

    private long br;

    @ApiModelProperty(value = "大小")
    private long size;

    @ApiModelProperty(value = "md5编码")
    private String md5;

    @ApiModelProperty(value = "编码", example = "200")
    private int code;

    @ApiModelProperty(value = "经验", example = "1200")
    private int expi;

    @ApiModelProperty(value = "歌曲类型", example = "mp3")
    private String type;

    @ApiModelProperty(value = "", example = "-8.111")
    private double gain;

    private int peak;
    private int fee;
    private String uf;
    private int payed;
    private int flag;

    @ApiModelProperty(value = "", example = "-8.111")
    private boolean canExtend;
    private String freeTrialInfo;
    @ApiModelProperty(value = "等级", example = "standard")
    private String level;
    @ApiModelProperty(value = "编码类型", example = "mp3")
    private String encodeType;
    private FreeTrialPrivilege freeTrialPrivilege;
    private FreeTimeTrialPrivilege freeTimeTrialPrivilege;
    private int urlSource;
    private int rightSource;
    private String podcastCtrp;
    private String effectTypes;

    @ApiModelProperty(value = "歌曲长度", example = "312997", notes = "毫秒")
    private long time;
}
