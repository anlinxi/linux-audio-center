package com.faker.audioStation.model.dto;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * mybatis plus 属性类型映射
 */
@Data
@NoArgsConstructor
public class ModelField {

    /**
     * 属性名称
     */
    private String modelName;

    /**
     * 属性类型
     */
    private Class<?> modelType;

    /**
     * 表字段名称
     */
    private String tableField;

    /**
     * 字段注释
     */
    private String apiModelProperty;

    /**
     * 是否是主键
     *
     * @return
     */
    private boolean pk;

    /**
     * 是否是唯一索引
     *
     * @return
     */
    private boolean unique;

    /**
     * 字段长度
     *
     * @return
     */
    private Integer length;

}
