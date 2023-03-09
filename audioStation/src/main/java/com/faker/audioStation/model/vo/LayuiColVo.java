package com.faker.audioStation.model.vo;

import com.faker.audioStation.util.ToolsUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@ApiModel("layui列表行参数")
public class LayuiColVo {

    @ApiModelProperty("属性名称")
    private String field;

    @ApiModelProperty("是否是主键")
    private boolean isPk;

    @ApiModelProperty("表字段名称")
    private String tableField;

    @ApiModelProperty("显示标题")
    private String title;

    @ApiModelProperty("宽度")
    private Integer width;

    @ApiModelProperty(value = "编辑类型", example = "text", notes = "text（单行输入框）\n" +
            "textarea（多行输入框）")
    private String edit;

    @ApiModelProperty(value = "是否允许排序", example = "true", notes = "如果设置 true，则在对应的表头显示排序icon，从而对列开启排序功能。")
    private boolean sort = true;

    @ApiModelProperty("字段类型")
    private String dataType;

    @ApiModelProperty("修正对其方式")
    private String fixed;

    @ApiModelProperty("最小宽度")
    private Integer minWidth;

    @ApiModelProperty("工具栏id")
    private String toolbar;

    @ApiModelProperty(value = "是否初始隐藏列", example = "false")
    private Boolean hide = false;

    @ApiModelProperty(value = "自定义列模板", name = "模板遵循 laytpl 语法", example = "#titleTpl")
    private String templet;

    public LayuiColVo(Object columnName, Object comments, Object dataType) {
        this.tableField = ToolsUtil.getString(columnName);
        this.title = ToolsUtil.getString(comments);
        this.dataType = ToolsUtil.getString(dataType);
        if ("".equals(this.title)) {
            this.title = this.tableField;
        }
    }

}
