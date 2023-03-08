package com.inspur.code.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "code")
@PropertySource("classpath:third-party-tools.properties")
@Configuration
@Data
public class ThirdPartyToolsConfig extends AbsConfig {
    private Map<String, String> third_tools;

    @Override
    public Map<String, String> getConfigMap() {
        return getThird_tools();
    }
}
