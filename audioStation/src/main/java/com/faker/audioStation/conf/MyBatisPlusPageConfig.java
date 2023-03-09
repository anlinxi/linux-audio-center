package com.faker.audioStation.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分页插件
 * 自动配配对应的数据库
 */
@Slf4j
@Configuration
public class MyBatisPlusPageConfig {

    @Value("${spring.datasource.driver-class-name}")
    @ApiModelProperty("驱动名称")
    private String driverClassName;

    /**
     * 防止 修改与删除时对全表进行操作
     *
     * @return
     */
    @Bean
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }


    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if ("com.mysql.cj.jdbc.Driver".equals(driverClassName)) {
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MARIADB));
        } else if ("oracle.jdbc.OracleDriver".equals(driverClassName)) {
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE_12C));
        } else if ("org.sqlite.JDBC".equals(driverClassName)) {
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQLITE));
        } else {
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        }
        log.info("分页插件读取驱动信息:" + driverClassName);
        return interceptor;
    }


    /**
     * ConfigurationCustomizer，这里引用的是MyBatisPlus自定义的一个和MyBatis同名的接口，com.baomidou.mybatisplus.spring.boot.starter.ConfigurationCustomizer，
     * 因此必须使用MyBatisPlus的ConfigurationCustomizer才行
     *
     * @return
     */
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                configuration.setCacheEnabled(true);
                configuration.setMapUnderscoreToCamelCase(true);
                configuration.setCallSettersOnNulls(true);
                configuration.setJdbcTypeForNull(JdbcType.NULL);
            }
        };
    }
}
