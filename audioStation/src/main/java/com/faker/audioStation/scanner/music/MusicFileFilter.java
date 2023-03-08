package com.faker.audioStation.scanner.music;

import io.swagger.annotations.ApiModelProperty;

import java.io.File;
import java.io.FileFilter;

/**
 * 音频文件扫描过滤器
 */
public class MusicFileFilter implements FileFilter {

    @ApiModelProperty("同意扫描的后缀")
    private String[] allowSuffixs = new String[]{"mp3", "arr", "flac"};

    /**
     * Tests whether or not the specified abstract pathname should be
     * included in a pathname list.
     *
     * @param pathname The abstract pathname to be tested
     * @return <code>true</code> if and only if <code>pathname</code>
     * should be included
     */
    @Override
    public boolean accept(File pathname) {
        //查看是否匹配
        for (String allowSuffix : allowSuffixs) {
            if (pathname.getName().toLowerCase().endsWith("." + allowSuffix)) {
                return true;
            }
        }
        return false;
    }
}
