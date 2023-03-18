package com.faker.audioStation.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.faker.audioStation.model.ano.SqliteCreater;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>MV信息对象</p>
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
 * @date 2023/3/9 11:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MV")
@ApiModel(value = "MV信息对象", description = "")
public class Mv implements Serializable {

    @ApiModelProperty("网易云音乐id")
    @Excel(name = "网易云音乐id")
    @TableId(value = "WYY_ID", type = IdType.INPUT)
    private Long wyyId;

    @ApiModelProperty("mv路径")
    @Excel(name = "mv路径")
    @TableField("PATH")
    private String path;

    @ApiModelProperty(value = "mv名称", notes = "")
    @Excel(name = "mv名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "mv原网址", notes = "")
    @Excel(name = "mv原网址")
    @TableField("URL")
    private String url;

    @ApiModelProperty(value = "mv大小", notes = "")
    @Excel(name = "mv大小")
    @TableField("size")
    private Long size;

    @ApiModelProperty(value = "视频分辨率", notes = "720/1080", example = "1080")
    @Excel(name = "清晰度")
    @TableField("CLARITY")
    private String resolution;

    @ApiModelProperty(value = "艺术家名称")
    @Excel(name = "艺术家")
    @TableField("ARTIST_NAME")
    private String artistName;

    @ApiModelProperty(value = "简介")
    @Excel(name = "简介")
    @TableField("DESC")
    private String desc;

    @ApiModelProperty(value = "发布时间", example = "2008-01-01")
    @Excel(name = "发布时间")
    @TableField("PUBLISH_TIME")
    private String publishTime;

}
