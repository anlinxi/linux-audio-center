package com.faker.audioStation.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableWebMvc
@EnableSwagger2
public class Swagger2Configuration implements WebMvcConfigurer {

    /**
     * api接口包扫描路径
     */
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.faker.audioStation";
    /**
     * 版本
     */
    public static final String VERSION = "1.0.0";

    /**
     * 日志对象
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 函数创建 Docket 的Bean
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        logger.info("Swagger2初始化......................");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }

    /**
     * 用来创建该 Api 的基本信息（这些基本信息会展现在文档页面中）
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("娱乐中心") //设置文档的标题
                .description("娱乐中心 API 接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .termsOfServiceUrl("https://www.anlinxi.com/") // 设置文档的License信息->1.3 License information
                .build();
    }
}
