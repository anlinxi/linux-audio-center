/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.faker.audioStation.conf;


import com.faker.audioStation.filter.MobileAuthFilter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MobileAuthConfig {

    /**
     * Bean化 注入stringRedisTemplate
     *
     * @return
     */
    @Bean
    public MobileAuthFilter getMobileAuthFilterBean() {
        return new MobileAuthFilter();
    }

    /**
     * 移动端权限管理控制器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<MobileAuthFilter> mobileAuthFilter() {
        FilterRegistrationBean<MobileAuthFilter> bean = new FilterRegistrationBean<>();
        bean.addUrlPatterns("/mobile/*", "/music/*");
        bean.setFilter(this.getMobileAuthFilterBean());
        //过滤器名称
        bean.setName("MobileAuthFilter");
//		bean.addInitParameter("sessionStatEnable", "false");
        //优先级，越低越优先
        bean.setOrder(10);
        log.debug("添加移动端权限管理过滤器完毕---------------------------------------------------------");
        return bean;
    }
}
