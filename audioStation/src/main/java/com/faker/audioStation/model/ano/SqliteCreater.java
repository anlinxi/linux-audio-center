package com.faker.audioStation.model.ano;

import java.lang.annotation.*;

/**
 * <p>Sqlite自动创建参数</p>
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
 * @date 2023/3/10 23:50
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface SqliteCreater {

    /**
     * 字段名
     *
     * @return
     */
    String value() default "";

    /**
     * 是否是主键
     *
     * @return
     */
    boolean isPk() default false;

    /**
     * 是否是唯一索引
     *
     * @return
     */
    boolean unique() default false;

    /**
     * 字段长度
     *
     * @return
     */
    int length() default -1;
}
