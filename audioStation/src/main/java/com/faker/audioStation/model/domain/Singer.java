package com.faker.audioStation.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>Singer</p>
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
 * @date 2023/3/9 11:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SINGER")
@ApiModel(value = "歌手信息对象", description = "歌手信息表")
public class Singer implements Serializable {

    @ApiModelProperty("主键")
    @Excel(name = "主键")
    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("姓名")
    @Excel(name = "姓名")
    @TableField("NAME")
    private String name;

    @ApiModelProperty("性别")
    @Excel(name = "性别")
    @TableField("SEX")
    private String sex;

    @ApiModelProperty("头像")
    @Excel(name = "头像")
    @TableField("PIC_PATH")
    private String pic;

    @ApiModelProperty("生日")
    @Excel(name = "生日")
    @TableField("BIRTH")
    private String birth;

    @ApiModelProperty("地区")
    @Excel(name = "地区")
    @TableField("LOCATION")
    private String location;

    @ApiModelProperty("简介")
    @Excel(name = "简介")
    @TableField("INTRODUCTION")
    private String introduction;

    @ApiModelProperty("音乐数量")
    @Excel(name = "音乐数量")
    @TableField("MUSIC_SIZE")
    private Long musicSize;

    @ApiModelProperty("专辑数量")
    @Excel(name = "专辑数量")
    @TableField("ALBUM_SIZE")
    private Long albumSize;

    @ApiModelProperty("网易云音乐id")
    @Excel(name = "网易云音乐id")
    @TableField("WYY_ID")
    private Long wyy_id;
}
