package com.faker.audioStation.model.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>MusicDto</p>
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
 * @date 2023/3/9 10:49
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MUSIC")
@ApiModel(value = "音频文件信息对象", description = "音频文件信息表")
public class MusicDto implements Serializable {

    @ApiModelProperty("音频文件哈希值")
    @Excel(name = "音频文件哈希值")
    @TableId(value = "HASH_CODE", type = IdType.ASSIGN_ID)
    private String hashCode;

    @ApiModelProperty("音频文件路径")
    @Excel(name = "音频文件路径")
    @TableField("PATH")
    private String path;

    @ApiModelProperty(value = "歌曲名称", notes = "表示内容为这首歌的标题,下同;")
    @Excel(name = "歌曲名称")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "艺术家", notes = "艺术家名称")
    @Excel(name = "艺术家")
    @TableField("ARTIST")
    private String artist;

    @ApiModelProperty(value = "艺术家id", notes = "艺术家id")
    @Excel(name = "艺术家")
    @TableField("ARTIST_ID")
    private String artistId;

    @ApiModelProperty(value = "专辑名称", notes = "专辑名称")
    @Excel(name = "专辑")
    @TableField("ALBUM")
    private String album;

    @ApiModelProperty(value = "专辑id", notes = "专辑id")
    @Excel(name = "专辑")
    @TableField("ALBUM_ID")
    private String albumId;

    @ApiModelProperty(value = "采样率", example = "320kbps", notes = "kbps")
    @Excel(name = "采样率")
    @TableField("RATE")
    private String rate;

    @ApiModelProperty(value = "歌曲长度", example = "291", notes = "单位:秒")
    @Excel(name = "歌曲长度")
    @TableField("SONG_LENGTH")
    private Integer songLength;

    @ApiModelProperty(value = "制作格式或制作技术", example = "Format", notes = "")
    @Excel(name = "制作格式或制作技术")
    @TableField("FORMAT")
    private String format;

    @ApiModelProperty(value = "音轨", example = "TRCK", notes = "格式:N/M 其中 N 为专集中的第 N 首,M 为专集中共 M 首,N 和 M 为 ASCII 码表示的数字")
    @Excel(name = "音轨")
    @TableField("TRACK")
    private String track;

    @ApiModelProperty(value = "年代", example = "TYER", notes = "是用 ASCII 码表示的数字")
    @Excel(name = "年代")
    @TableField("YEARS")
    private String years;

    @ApiModelProperty(value = "类型", example = "TCON", notes = "直接用字符串表示")
    @Excel(name = "类型")
    @TableField("TYPE")
    private String type;

    @ApiModelProperty(value = "备注", example = "COMM", notes = "格式:\"eng/0 备注内容\",其中 eng 表示备注所使用的自然语言")
    @Excel(name = "备注")
    @TableField("NOTE")
    private String note;

    @ApiModelProperty(value = "语言", example = "TYER", notes = "LANGUAGE")
    @Excel(name = "语言")
    @TableField("LANGUAGE")
    private String language;

    @ApiModelProperty(value = "歌词", example = "TYER", notes = "LANGUAGE")
    @Excel(name = "歌词")
    @TableField("LYRIC")
    private String lyric;

    @ApiModelProperty(value = "音频封面图片", notes = "音频封面图片id")
    @Excel(name = "音频封面图片")
    @TableField("COVER_ID")
    private String coverId;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
