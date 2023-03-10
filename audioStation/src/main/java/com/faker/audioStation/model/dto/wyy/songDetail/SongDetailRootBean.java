/**
 * Copyright 2023 bejson.com
 */
package com.faker.audioStation.model.dto.wyy.songDetail;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2023-03-10 21:38:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@NoArgsConstructor
@ApiModel("网易云返回歌曲详情对象")
public class SongDetailRootBean implements Serializable {

    private List<Privileges> privileges;
    private int code;
    private List<Songs> songs;

}