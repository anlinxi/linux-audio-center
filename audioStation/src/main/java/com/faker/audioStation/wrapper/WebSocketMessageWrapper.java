package com.faker.audioStation.wrapper;

import com.faker.audioStation.util.IdGen;
import com.faker.audioStation.websocket.model.Message;
import io.swagger.annotations.ApiModelProperty;

import java.util.Random;

public class WebSocketMessageWrapper {

    @ApiModelProperty("随机数对象")
    private static Random random = new Random();

    /**
     * 获取一个消息对象
     *
     * @param id        消息id
     * @param action    消息类型，用户自定义消息类别
     * @param title     消息标题
     * @param content   消息类容，于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
     * @param sender    消息发送者账号
     * @param receiver  消息发送者接收者
     * @param extra     附加内容 内容
     * @param format    content 内容格式
     * @param timestamp 时间戳
     * @return
     */
    public static Message msg(String id, String action, String title, String content, String sender, String receiver, String extra,
                              String format, long timestamp) {
        Message message = new Message();
        message.setId(String.valueOf(id));
        message.setAction(action);
        message.setTitle(title);
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setExtra(extra);
        message.setFormat(format);
        message.setTimestamp(timestamp);
        return message;
    }

    /**
     * 获取一个消息对象
     *
     * @param action   消息类型，用户自定义消息类别
     * @param title    消息标题
     * @param content  消息类容，于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
     * @param sender   消息发送者账号
     * @param receiver 消息发送者接收者
     * @param extra    附加内容 内容
     * @param format   content 内容格式
     * @return
     */
    public static Message msg(String action, String title, String content, String sender, String receiver, String extra, String format) {
        long timestamp = System.currentTimeMillis();
        return msg(IdGen.nextId(), action, title, content, sender, receiver, extra, format, timestamp);
    }


    /**
     * 获取一个消息对象
     *
     * @param action   消息类型，用户自定义消息类别
     * @param title    消息标题
     * @param content  消息类容，于action 组合为任何类型消息，content 根据 format 可表示为 text,json ,xml数据格式
     * @param sender   消息发送者账号
     * @param receiver 消息发送者接收者
     * @return
     */
    public static Message msg(String action, String title, String content, String sender, String receiver) {
        String format = "text";
        return msg(action, title, content, sender, receiver, null, format);
    }

    /**
     * 错误消息对象
     *
     * @param title    消息标题
     * @param content  消息类容
     * @param receiver 消息接收者
     * @return
     */
    public static Message error(String title, String content, String receiver) {
        String action = "error";
        String sender = "system";
        return msg(action, title, content, sender, receiver);
    }

    /**
     * 错误消息对象
     *
     * @param content 消息类容
     * @return
     */
    public static Message error(String content) {
        String title = "错误";
        return error(title, content, null);
    }

    /**
     * 错误消息对象
     *
     * @param title    消息标题
     * @param content  消息类容
     * @param receiver 消息接收者
     * @return
     */
    public static Message ok(String title, String content, String receiver) {
        String action = "ok";
        String sender = "system";
        return msg(action, title, content, sender, receiver);
    }

    /**
     * 错误消息对象
     *
     * @param content 消息接收者
     * @return
     */
    public static Message ok(String content) {
        String title = "成功";
        return ok(title, content, null);
    }

}
