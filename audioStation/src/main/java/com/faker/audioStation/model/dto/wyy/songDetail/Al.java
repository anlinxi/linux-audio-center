/**
 * Copyright 2023 bejson.com
 */
package com.faker.audioStation.model.dto.wyy.songDetail;

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
public class Al implements Serializable {

    private String picUrl;
    private String name;
    private List<String> tns;
    private String pic_str;
    private long id;
    private long pic;

}