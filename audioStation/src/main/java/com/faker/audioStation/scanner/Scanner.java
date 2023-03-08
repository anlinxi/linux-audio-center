package com.faker.audioStation.scanner;

import com.faker.audioStation.wrapper.Wrapper;

import java.io.File;

/**
 * 扫描器
 */
public interface Scanner {

    /**
     * 开始扫描
     *
     * @param path 扫描路径
     * @return
     */
    Wrapper startScan(String path);

    /**
     * 递归扫描文件
     *
     * @param file
     */
    void scanFile(File file);
}
