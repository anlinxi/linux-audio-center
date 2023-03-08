package com.inspur.code.generator.config;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsConfig {

    public String getValue(String key) {
        if (key == null || "".equalsIgnoreCase(key.trim())) {
            return null;
        }
        key = key.trim();
        if (formatMap().containsKey(key)) {
            String value = formatMap().get(key);
            if (value == null || "".equals(value.trim()))
                return null;
            return value;
        }
        return null;
    }

    private Map<String, String> formatMap() {
        Map<String, String> configMap = getConfigMap();
        if (configMap == null) return new HashMap<>();
        return configMap;
    }

    public abstract Map<String, String> getConfigMap();
}
