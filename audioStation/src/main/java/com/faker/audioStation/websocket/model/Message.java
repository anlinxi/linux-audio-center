/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.faker.audioStation.websocket.model;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息对象
 */
@Data
public class Message implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息id")
    private String id;

    @ApiModelProperty(value = "消息类型", notes = "用户自定义消息类别", example = "sendMessage")
    private String action;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty(value = "消息类容", notes = "于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式")
    private String content;

    @ApiModelProperty("消息发送者账号")
    private String sender;

    @ApiModelProperty("消息接收者账号")
    private String receiver;

    @ApiModelProperty(value = "content 内容格式", notes = "text,json,xml", example = "json")
    private String format;

    @ApiModelProperty("附加内容")
    private String extra;

    @ApiModelProperty("时间戳")
    private long timestamp;

    public Message() {
        timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("#Message#").append("\n");
        buffer.append("id       :").append(id).append("\n");
        buffer.append("sender   :").append(sender).append("\n");
        buffer.append("receiver :").append(receiver).append("\n");
        buffer.append("action   :").append(action).append("\n");
        buffer.append("content  :").append(content).append("\n");
        buffer.append("format   :").append(format).append("\n");
        buffer.append("extra    :").append(extra).append("\n");
        buffer.append("title    :").append(title).append("\n");
        buffer.append("timestamp:").append(timestamp);
        return buffer.toString();
    }

    @Override
    public Message clone() {
        Message message = new Message();
        BeanUtil.copyProperties(this, message);
        return message;
    }

    public String jsonString() {
        return JSONObject.toJSONString(this);
    }
}
