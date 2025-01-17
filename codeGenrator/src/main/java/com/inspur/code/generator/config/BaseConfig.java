package com.inspur.code.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;


@ConfigurationProperties(prefix = "code")
@PropertySource("classpath:config.properties")
@Configuration
@Data
public class BaseConfig extends AbsConfig {
    private Map<String, String> base;


    @Override
    public Map<String, String> getConfigMap() {
        return getBase();
    }

    public Map<String, String> getBase() {
        if (this.base == null) {
            base = new HashMap<>();
            base.put("AUTHOR", System.getProperty("user.name"));
        }
        return base;
    }
}
