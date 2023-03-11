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
 * <p>歌词信息对象</p>
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
@TableName("LYRIC")
@ApiModel(value = "歌词信息对象", description = "")
public class Lyric implements Serializable {

    @ApiModelProperty("主键")
    @Excel(name = "主键")
    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty("歌词路径")
    @Excel(name = "歌词路径")
    @TableField("PATH")
    private String path;

    @ApiModelProperty(value = "歌词名称", notes = "")
    @Excel(name = "歌词名称")
    @TableField("NAME")
    private String name;

    @SqliteCreater(unique = true)
    @ApiModelProperty("网易云音乐id")
    @Excel(name = "网易云音乐id")
    @TableField("WYY_ID")
    private Long wyyId;
}
