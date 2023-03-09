package com.faker.audioStation.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.faker.audioStation.model.dto.ModelField;
import com.faker.audioStation.model.dto.SqliteTableStructureDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>MyBatisPlus支持sqlite初始化建表</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/8</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/8 14:19
 */
@Slf4j
public class MyBatisPlusSuppotSqliteInit {


    /**
     * 单里模式
     */
    private static MyBatisPlusSuppotSqliteInit myBatisPlusSuppotSqliteInit = null;

    /**
     * 私有化构造函数，使用getInstance()去获取实例
     */
    private MyBatisPlusSuppotSqliteInit() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static MyBatisPlusSuppotSqliteInit getInstance() {
        if (null == myBatisPlusSuppotSqliteInit) {
            myBatisPlusSuppotSqliteInit = new MyBatisPlusSuppotSqliteInit();
        }
        return myBatisPlusSuppotSqliteInit;
    }

    @Data
    @ApiModel("mybatisPlus对象")
    public class MybatisPlusDto {
        //表名
        String tableName = null;
        //主键对应的字段名
        String tableId = null;
        //表字段结构
        List<ModelField> modelFieldList = new ArrayList();
        //表字段名称列表
        List<String> columnList = new ArrayList<String>();
    }

    /**
     * 获取实例
     *
     * @param clazz
     * @return
     */
    public MybatisPlusDto getMybatisPlusDto(Class clazz) {
        MybatisPlusDto mybatisPlusDto = new MybatisPlusDto();

        //检查实体类是否缺少注解
        boolean isTableName = clazz.isAnnotationPresent(TableName.class);
        if (isTableName) {
            TableName tableNameIn = (TableName) clazz.getAnnotation(TableName.class);
            if (null == tableNameIn.value() || "".equals(tableNameIn.value())) {
                throw new RuntimeException("实体类无TableName注解！");
            }
            mybatisPlusDto.tableName = tableNameIn.value();
        }
        //是否包含注解
        boolean isTableField = false;
        boolean isTableId = false;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (isTableField == false) {
                isTableField = fields[i].isAnnotationPresent(TableField.class);
            }
            if (isTableId == false) {
                isTableId = fields[i].isAnnotationPresent(TableId.class);
            }
            if (isTableField && isTableId) {
                //都找到注解了 就终止
                break;
            }
        }
        if (!isTableField) {
            throw new RuntimeException("实体类无TableField注解！");
        }

        if (!isTableId) {
            log.warn("实体类无isTableId注解！");
        }

        //获取表结构
        for (int i = 0; i < fields.length; i++) {
            ModelField modelField = new ModelField();
            modelField.setModelName(fields[i].getName());
            modelField.setModelType(fields[i].getType());
            boolean annotationPresent = fields[i].isAnnotationPresent(TableField.class);
            if (annotationPresent) {
                // 获取注解值
                String tableField = fields[i].getAnnotation(TableField.class).value();
                if (null != tableField && !"".equals(tableField)) {
                    modelField.setTableField(tableField.toUpperCase());
                    mybatisPlusDto.columnList.add(tableField.toUpperCase());
                    boolean apiMp = fields[i].isAnnotationPresent(ApiModelProperty.class);
                    if (apiMp) {
                        modelField.setApiModelProperty(fields[i].getAnnotation(ApiModelProperty.class).value());
                    } else {
                        log.debug("类" + clazz.getName() + "的字段" + fields[i].getName() + "没有注解ApiModelProperty");
                    }
                    mybatisPlusDto.modelFieldList.add(modelField);
                } else {
                    log.warn("属性[" + modelField.getModelName() + "]对应表字段为空！");
                }
            } else if (fields[i].isAnnotationPresent(TableId.class)) {
                // 获取注解值
                mybatisPlusDto.tableId = fields[i].getAnnotation(TableId.class).value().toUpperCase();
                if (null != mybatisPlusDto.tableId && !"".equals(mybatisPlusDto.tableId)) {
                    modelField.setTableField(mybatisPlusDto.tableId.toUpperCase());
                    mybatisPlusDto.columnList.add(mybatisPlusDto.tableId.toUpperCase());
                    boolean apiMp = fields[i].isAnnotationPresent(ApiModelProperty.class);
                    if (apiMp) {
                        modelField.setApiModelProperty(fields[i].getAnnotation(ApiModelProperty.class).value());
                    } else {
                        log.debug("类" + clazz.getName() + "的字段" + fields[i].getName() + "没有注解ApiModelProperty");
                    }
                    mybatisPlusDto.modelFieldList.add(modelField);
                } else {
                    log.warn("属性[" + modelField.getModelName() + "]对应表字段为空！");
                }
            }
        }
//        log.debug("类[" + clazz.getName() + "]的表名为[" + mybatisPlusDto.tableName + "];字段信息为:" + mybatisPlusDto.modelFieldList);
        return mybatisPlusDto;
    }


    /**
     * 建表
     *
     * @param clazz
     */
    public String createTable(Class clazz) {
        MybatisPlusDto dto = this.getMybatisPlusDto(clazz);
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE ").append("\"").append(dto.getTableName()).append("\" (\n");
        //表字段结构
        List<ModelField> modelFieldList = dto.getModelFieldList();
        for (ModelField modelField : modelFieldList) {
            if (modelField.getTableField().equals(dto.getTableId())) {
                if (modelField.getModelType().equals(Integer.class)
                        || modelField.getModelType().equals(int.class)
                        || modelField.getModelType().equals(Long.class)
                        || modelField.getModelType().equals(long.class)
                ) {
                    sql.append("\"" + modelField.getTableField() + "\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n");
                } else {
                    sql.append("\"" + modelField.getTableField() + "\" TEXT NOT NULL PRIMARY KEY,\n");
                }
            } else {
                sql.append("\"" + modelField.getTableField() + "\" ").append(this.getSqliteType(modelField.getModelType())).append(",\n");
            }
        }
        sql.setLength(sql.length() - 2);
        sql.append(");\n");
//        log.info(sql.toString());
        return sql.toString();
    }

    /**
     * 转换数据类型为sqlite类型
     *
     * @param aClass
     * @return
     */
    private String getSqliteType(Class aClass) {
        if (aClass.equals(Integer.class)
                || aClass.equals(int.class)
                || aClass.equals(Long.class)
                || aClass.equals(long.class)
        ) {
            return "INTEGER";
        }
        if (aClass.equals(Float.class)
                || aClass.equals(float.class)
                || aClass.equals(Double.class)
                || aClass.equals(double.class)
        ) {
            return "REAL";
        }
        if (aClass.equals(Date.class)) {
            return "NUMERIC";
        }
        if (aClass.equals(String.class)) {
            return "TEXT";
        }
        if (aClass.equals(Boolean.class)
                || aClass.equals(boolean.class)) {
            return "INTEGER";
        }
        return "NUMERIC";
    }

    /**
     * 创建表字段
     *
     * @param clazz
     * @param sqliteList
     * @return
     */
    public List<String> createField(Class clazz, List<SqliteTableStructureDto> sqliteList) {
        List<String> sqlList = new ArrayList<String>();
        List<String> fields = sqliteList.stream().map(item -> item.getName().toUpperCase()).collect(Collectors.toList());
        MybatisPlusDto dto = this.getMybatisPlusDto(clazz);
        StringBuffer sql = new StringBuffer();
        //表字段结构
        List<ModelField> modelFieldList = dto.getModelFieldList();
        for (ModelField modelField : modelFieldList) {
            if (!fields.contains(modelField.getTableField().toUpperCase())) {
                log.warn("表[" + dto.getTableName() + "]字段[" + modelField.getTableField().toUpperCase() + "]缺失，正在生成重建sql");
                sqlList.add("alter table \"" + dto.getTableName() + "\" add \"" + modelField.getTableField().toUpperCase() + "\" "
                        + this.getSqliteType(modelField.getModelType()) + "");
            }
        }
        return sqlList;
    }


}
