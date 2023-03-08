package com.inspur.code.generator.domain;

import lombok.Data;

/**
 * 模版定义
 */
@Data
public class TempletItem {
    /**
     * 模版名称
     */
    private String name;
    private String relativePath;
    private String outPath;
    private String outFileName;
}
