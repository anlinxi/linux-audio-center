package com.faker.audioStation.scanner.music;

import com.alibaba.fastjson.JSONObject;
import com.faker.audioStation.model.dto.AudioScanInfoDto;
import com.faker.audioStation.scanner.Scanner;
import com.faker.audioStation.util.MediaInfoHandler;
import com.faker.audioStation.wrapper.WrapMapper;
import com.faker.audioStation.wrapper.Wrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.asf.AsfFileReader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.mp4.Mp4FileReader;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.real.RealFileReader;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
//        MediaInfoHandler mediaInfoHandler = new MediaInfoHandler();
        int index = 0;
        List<AudioScanInfoDto> audioScanInfoDtoList = new ArrayList<>();
        for (File audio : audioList.get()) {
//            mediaInfoHandler.handle(audio);
//            HashMap hashMap = mediaInfoHandler.getMediaInfo();
//            AudioScanInfoDto audioScanInfoDto = new AudioScanInfoDto(hashMap);
//            audioScanInfoDto.setCover(mediaInfoHandler.getImage());
//            log.info("得到音频的信息:" + JSONObject.toJSONString(audioScanInfoDto));
//            log.info("得到音频的信息Map:" + JSONObject.toJSONString(hashMap));
            AudioFileReader audioFileReader = null;
            String audioName = audio.getName().toLowerCase();
            if (audioName.endsWith(".mp3")) {
                audioFileReader = new MP3FileReader();
            } else if (audioName.endsWith(".mp4")) {
                audioFileReader = new Mp4FileReader();
            } else if (audioName.endsWith(".flac")) {
                audioFileReader = new FlacFileReader();
            } else if (audioName.endsWith(".asf")) {
                audioFileReader = new AsfFileReader();
            } else if (audioName.endsWith(".ogg")) {
                audioFileReader = new OggFileReader();
            } else if (audioName.endsWith(".real")) {
                audioFileReader = new RealFileReader();
            } else if (audioName.endsWith(".wav")) {
                audioFileReader = new WavFileReader();
            } else {
                continue;
            }
            try {
                AudioFile audioFile = audioFileReader.read(audio);
                AudioScanInfoDto audioScanInfoDto = new AudioScanInfoDto(audioFile);
                audioScanInfoDtoList.add(audioScanInfoDto);
            } catch (CannotReadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (index++ > 3) {
                break;
            }
        }

        //todo 查询下载歌曲歌词

        //todo 归纳歌曲专辑/歌手信息

        //todo 查询下载专辑封面/歌手封面

        //todo 爬取歌曲热度、播放量、评论等额外信息
        return WrapMapper.ok();
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
