package com.inspur.code.generator.serivce;

import com.inspur.code.generator.config.BaseConfig;
import com.inspur.code.generator.config.GeraltorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeneratorService {
    @Autowired
    GeraltorConfig geraltorConfig;
    @Autowired
    BaseConfig baseConfig;
    @Autowired
    Generator generator;

    public void genralCode() {
        String outdir = geraltorConfig.getValue("outputDir");
        log.info("genralCode start ，output dir: {}", outdir);
        try {
            generator.execute();
        } catch (Exception e) {
            log.error("genralCode error：", e);
        }
        log.info("genralCode finish ，output dir:{}", outdir);
    }
}
