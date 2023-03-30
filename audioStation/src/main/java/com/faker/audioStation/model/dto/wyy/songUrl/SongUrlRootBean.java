package com.faker.audioStation.model.dto.wyy.songUrl;

import com.faker.audioStation.model.domain.Music;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>JsonRootBean</p>
 *
 * <p>项目名称：audioCenter</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2023/3/10</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2023/3/10 20:50
 */
@Data
@NoArgsConstructor
@ApiModel("网易云返回对象")
public class SongUrlRootBean implements Serializable {

    @ApiModelProperty(value = "返回数据")
    private List<JsonData> data;

    @ApiModelProperty(value = "返回编码", example = "200")
    private int code;

    /**
     * 歌曲类型转换
     *
     * @param music
     */
    public SongUrlRootBean(Music music) {
        this.code = 200;
        this.data = new ArrayList<JsonData>();
        JsonData jsonData = new JsonData();
        jsonData.setId(music.getWyyId());
        jsonData.setUrl("/api/music/getMusic?id=" + music.getId());
        jsonData.setBr(0L);
        jsonData.setSize(music.getSongLength());
        jsonData.setMd5("");
        jsonData.setCode(200);
        jsonData.setExpi(0);
        jsonData.setType(music.getType());
        jsonData.setGain(0.0D);
        jsonData.setPeak(0);
        jsonData.setFee(3);
        jsonData.setUf("");
        jsonData.setPayed(0);
        jsonData.setFlag(0);
        jsonData.setCanExtend(false);
        jsonData.setFreeTrialInfo("");
        jsonData.setLevel("standard");
        jsonData.setEncodeType("mp3");
        jsonData.setFreeTrialPrivilege(new FreeTrialPrivilege());
        jsonData.setFreeTimeTrialPrivilege(new FreeTimeTrialPrivilege());
        jsonData.setUrlSource(0);
        jsonData.setRightSource(0);
        jsonData.setPodcastCtrp("");
        jsonData.setEffectTypes("");
        jsonData.setTime(music.getSongLength());

        this.data.add(jsonData);
    }
}
