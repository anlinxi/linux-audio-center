package com.inspur.code.generator.job;

import com.inspur.code.generator.CodeGenApp;
import com.inspur.code.generator.serivce.GeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class GeneralJob implements ApplicationContextAware {


    /**
     * 运行context
     */
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    @Autowired
    private GeneratorService generatorService;

    @PostConstruct
    public void init() {
        log.info("代码生成开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        generatorService.genralCode();
        log.info("代码生成结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
        ctx.close();
        System.exit(0);
    }
}
