/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.faker.audioStation.conf;

import com.faker.audioStation.filter.MobileSecretFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 配置
 *
 * @author ThinkGem
 * @version 2017年11月30日
 */
@Configuration
@ConditionalOnProperty(
        name = {"state.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class MobileSecretConfig {

    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(MobileSecretConfig.class);

    /**
     * Bean化 注入stringRedisTemplate
     *
     * @return
     */
    @Bean
    public MobileSecretFilter getMobileFilterBean() {
        return new MobileSecretFilter();
    }

    /**
     * 移动端权限管理控制器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<MobileSecretFilter> mobileSecretFilter() {
        FilterRegistrationBean<MobileSecretFilter> bean = new FilterRegistrationBean<>();
        bean.addUrlPatterns("/mobile/*");
        bean.addUrlPatterns("/app/*");
        bean.setFilter(this.getMobileFilterBean());
        //过滤器名称
        bean.setName("MobileSecretConfig");
//		bean.addInitParameter("sessionStatEnable", "false");
        //优先级，越低越优先
        bean.setOrder(15);
        logger.info("添加移动端参数解密过滤器完毕---------------------------------------------------------");
        return bean;
    }
}
