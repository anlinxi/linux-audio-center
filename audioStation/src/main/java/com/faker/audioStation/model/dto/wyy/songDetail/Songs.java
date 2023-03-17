/**
 * Copyright 2023 bejson.com
 */
package com.faker.audioStation.model.dto.wyy.songDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2023-03-10 21:38:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@NoArgsConstructor
@ApiModel("网易云返回歌曲详情对象Songs")
public class Songs implements Serializable {

    @ApiModelProperty(value = "歌曲id", example = "1959528822")
    private Long id;

    @ApiModelProperty(value = "歌曲名称", example = "紫荆花盛开")
    private String name;

    @ApiModelProperty(value = "cd", example = "01")
    private String cd;

    @ApiModelProperty(value = "编号")
    private Long no;
    @ApiModelProperty(value = "版权信息")
    private Long copyright;
    private Long fee;
    private Long mst;
    private Long pst;
    private Long pop;
    private long dt;
    private Long rtype;
    private Long s_id;
    private List<String> rtUrls;
    @ApiModelProperty(value = "资源状态")
    private boolean resourceState;

    private Sq sq;
    private Long st;
    @ApiModelProperty(value = "发布时间戳", example = "1656518400000")
    private long publishTime;
    private String cf;
    private Long originCoverType;
    private H h;
    private long mv;
    @ApiModelProperty(value = "专辑信息")
    private Al al;
    private L l;
    private M m;
    @ApiModelProperty(value = "版本号", example = "20")
    private Long version;
    private Long cp;
    private List<String> alia;
    private Long djId;
    private Long single;
    @ApiModelProperty(value = "艺术家")
    private List<Ar> ar;
    private Long ftype;
    private Long t;
    private Long v;
    @ApiModelProperty(value = "标记型号", example = "536879104")
    private String mark;
}