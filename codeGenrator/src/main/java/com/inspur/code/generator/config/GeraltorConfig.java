package com.inspur.code.generator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "code")
@PropertySource("classpath:genrator.properties")
@Configuration
@Data
public class GeraltorConfig  extends AbsConfig{
    /**
     * 配置信息
     */
    private Map<String, String> genrator;

    // 表前缀，生成的实体类，不含前缀

    @Value("#{'${code.table.tablePrefixes}'.split(',')}")
    public String[] tablePrefixes;
    // 表名，为空，生成所有的表
    @Value("#{'${code.table.tableNames}'.split(',')}")
    public String[] tableNames;
    // 字段前缀
    @Value("#{'${code.table.fieldPrefixes}'.split(',')}")
    public String[] fieldPrefixes;


    @Override
    public Map<String, String> getConfigMap() {
        return getGenrator();
    }
}
