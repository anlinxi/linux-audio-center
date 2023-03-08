package com.inspur.wfmcloud.provider.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Map;

/**
 * MybatisPlus查询工具类
 */
public class MybatisPlusQueryUtil {

    /**
     * 提取params里查询参数并写入QueryWrapper里
     * 一个前端通用的写法
     * @param query 查询sql
     * @param params 参数
     * @return QueryWrapper
     */
    public static QueryWrapper getQueryFromParams(QueryWrapper query, Map<String, String> params){
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key != null && key.length() > 2) {
                String value = entry.getValue();
                if (key.startsWith("EQ_")) {
                    String column = key.substring(2, key.length());
                    query.eq(column, value);
                } else if (key.length() > 4  && key.startsWith("LIKE_")) {
                    String column = key.substring(4, key.length());
                    query.like(column,value);
                }
            }

        }
        return query;
    }
}
