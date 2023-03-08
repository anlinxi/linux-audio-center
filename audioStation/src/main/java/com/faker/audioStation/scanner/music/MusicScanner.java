package com.faker.audioStation.scanner.music;

import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐扫描器
 */
@Slf4j
@Component
public class MusicScanner implements Scanner {


    @ApiModelProperty("音乐文件列表")
    private ThreadLocal<List<File>> audioList = ThreadLocal.withInitial(() -> new ArrayList<File>());

    /**
     * 开始扫描音乐
     *
     * @param path 扫描路径
     * @return
     */
    @Override
    public Wrapper startScan(String path) {
        File fileList = new File(path);
        if (fileList.exists()) {
            if (fileList.isFile()) {
                audioList.get().add(fileList);
            } else if (fileList.isDirectory()) {
                this.scanFile(fileList);
            }
        }
        log.info("扫描到的音乐文件数量" + audioList.get().size());
        //todo 读取mp3信息

        //todo 查询下载歌曲歌词

        //todo 归纳歌曲专辑/歌手信息

        //todo 查询下载专辑封面/歌手封面

        //todo 爬取歌曲热度、播放量、评论等额外信息
        return WrapMapper.ok(audioList.get());
    }

    /**
     * 递归扫描文件
     *
     * @param file
     */
    public void scanFile(File file) {
        if (file.exists() && file.isDirectory()) {
            File[] dirs = file.listFiles(new MusicFileFilter());
            for (File child : dirs) {
                if (child.isFile()) {
                    audioList.get().add(child);
                } else if (child.isDirectory()) {
                    this.scanFile(child);
                }
            }
        }
    }
}
