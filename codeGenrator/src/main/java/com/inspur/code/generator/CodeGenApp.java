package com.inspur.code.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码生成主类
 */
@SpringBootApplication
@Slf4j
public class CodeGenApp {
    /**
     * 开始生产代码
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CodeGenApp.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
