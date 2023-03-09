package com.faker.audioStation.model.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>音频文件ID3扫描信息</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/8</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/8 10:23
 */
@Slf4j
@Data
@NoArgsConstructor
@ApiModel("音频文件ID3扫描信息")
public class AudioScanInfoDto {

    @ApiModelProperty("音频文件路径")
    private String path;

    @ApiModelProperty(value = "音频封面图片类型", example = "APIC", notes = "图片后缀名")
    private String apic;

    @ApiModelProperty("音频封面图片")
    private Image cover;

    @ApiModelProperty(value = "标题", example = "TIT2", notes = "表示内容为这首歌的标题,下同;")
    private String title;

    @ApiModelProperty(value = "作者", example = "TPE1")
    private String artist;

    @ApiModelProperty(value = "专辑", example = "TPE1")
    private String album;

    @ApiModelProperty(value = "采样率", example = "320kbps", notes = "kbps")
    private String rate;

    @ApiModelProperty(value = "歌曲长度", example = "291", notes = "秒")
    private Integer songLength;

    @ApiModelProperty(value = "制作格式或制作技术", example = "Format", notes = "")
    private String format;

    @ApiModelProperty(value = "音轨", example = "TRCK", notes = "格式:N/M 其中 N 为专集中的第 N 首,M 为专集中共 M 首,N 和 M 为 ASCII 码表示的数字")
    private String track;

    @ApiModelProperty(value = "年代", example = "TYER", notes = "是用 ASCII 码表示的数字")
    private String years;

    @ApiModelProperty(value = "类型", example = "TCON", notes = "直接用字符串表示")
    private String type;

    @ApiModelProperty(value = "备注", example = "COMM", notes = "格式:\"eng/0 备注内容\",其中 eng 表示备注所使用的自然语言")
    private String note;

    @ApiModelProperty(value = "语言", example = "TYER", notes = "LANGUAGE")
    private String language;


    public AudioScanInfoDto(HashMap hashMap) {
        this.path = this.valueOfSring(hashMap.get("PATH"));
        this.apic = this.valueOfSring(hashMap.get("APIC"));
        this.title = this.valueOfSring(hashMap.get("TIT2"));
        this.artist = this.valueOfSring(hashMap.get("TPE1"));
        this.album = this.valueOfSring(hashMap.get("TPE1"));
        this.track = this.valueOfSring(hashMap.get("TRCK"));
        this.years = this.valueOfSring(hashMap.get("TYER"));
        this.type = this.valueOfSring(hashMap.get("TCON"));
        this.note = this.valueOfSring(hashMap.get("COMM"));

    }

    public AudioScanInfoDto(AudioFile audioFile) {
        //采样率（SampleRate）、制作格式或制作技术（Format）、、、、、、语言（LANGUAGE）、版权方（COPYRIGHT）
        AudioHeader audioHeader = audioFile.getAudioHeader();
        this.rate = audioHeader.getBitRate();
        this.songLength = audioHeader.getTrackLength();
        this.format = audioHeader.getFormat();
        Tag tag = audioFile.getTag();
        this.path = audioFile.getFile().getAbsolutePath();
        //单曲名（TITLE）
        this.title = tag.getFirst(FieldKey.TITLE);
        //单曲艺术家（ARTIST）
        this.artist = tag.getFirst(FieldKey.ARTIST);
        //专辑名（ALBUM）
        this.album = tag.getFirst(FieldKey.ALBUM);
        //专辑艺术家（ALBUM_ARTIST）
        //音轨号（TRACK）
        this.track = tag.getFirst(FieldKey.TRACK);
        this.years = tag.getFirst(FieldKey.YEAR);
        //语言（LANGUAGE）
        this.language = tag.getFirst(FieldKey.LANGUAGE);
        //版权方（COPYRIGHT）

        tag.getFirst(FieldKey.LANGUAGE);

        log.info("得到音频的信息:" + JSONObject.toJSONString(this));

        //FlacTag对象的getFirstArtwork方法可以获得图片
        Artwork artwork = tag.getFirstArtwork();
        try {
            this.cover = artwork.getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转字符串
     *
     * @param object
     * @return
     */
    private String valueOfSring(Object object) {
        if (null == object) {
            return null;
        }
        return String.valueOf(object);
    }
}
