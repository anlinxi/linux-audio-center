package com.faker.audioStation.websocket;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.faker.audioStation.model.domain.JsMobileUser;
import com.faker.audioStation.service.IJsMobileUserService;
import com.faker.audioStation.util.SpringContextUtils;
import com.faker.audioStation.websocket.model.Message;
import com.faker.audioStation.wrapper.WebSocketMessageWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>WebsocketHandle</p>
 *
 * <p>项目名称：audioStation</p>
 *
 * <p>注释:无</p>
 *
 * <p>Copyright: Copyright Faker(c) 2022/10/8</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2022/10/8 9:46
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{token}")
@Api(value = "websocket消息推送工具")
public class WebsocketHandle {


    @ApiModelProperty("移动端用户")
    @Autowired
    private IJsMobileUserService iJsMobileUserService;

    @ApiModelProperty("客户端token")
    private String token;

    @ApiModelProperty("客户端userId")
    private String userId;

    @ApiModelProperty("客户端Session")
    private Session session;

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebsocketHandle> webSocketMap = new ConcurrentHashMap<String, WebsocketHandle>();

    /**
     * 新的WebSocket请求开启
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        log.info("新的WebSocket请求开启:" + token);
        try {
            if (null == iJsMobileUserService) {
                iJsMobileUserService = SpringContextUtils.getBean(IJsMobileUserService.class);
            }

            String userId = iJsMobileUserService.getUserIdByToken(token);
            if (StringUtils.isEmpty(userId)) {
                Message message = WebSocketMessageWrapper.error("token错误");
                session.getBasicRemote().sendText(message.jsonString());
                session.close();
            }
            QueryWrapper<JsMobileUser> jsMobileUserQueryWrapper = new QueryWrapper<>();
            jsMobileUserQueryWrapper.or().eq("user_code", userId).or().eq("login_code", userId);
            JsMobileUser jsMobileUser = iJsMobileUserService.getOne(jsMobileUserQueryWrapper);
            if (null == jsMobileUser) {
                Message message = WebSocketMessageWrapper.error("用户[" + userId + "]验证错误");
                session.getBasicRemote().sendText(message.jsonString());
                session.close();
            }
            userId = jsMobileUser.getUserCode();
            this.token = token;
            this.userId = userId;
            this.session = session;
            webSocketMap.put(userId, this);
            String userName = userId;

            Message message = WebSocketMessageWrapper.ok("用户[" + userName + "]连接成功!");
            message.setReceiver(userId);
            session.getBasicRemote().sendText(message.jsonString());

            //用户在线
            JsMobileUser updateUser = new JsMobileUser();
            updateUser.setUserCode(userId);
            updateUser.setExtendI1(1L);
            iJsMobileUserService.updateById(updateUser);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Message message = WebSocketMessageWrapper.error("token校验异常:" + e.getMessage());
                session.getBasicRemote().sendText(message.jsonString());
                session.close();
            } catch (IOException ex) {
                log.debug(ex.getMessage());
            }
        }
    }

    /**
     * WebSocket 请求关闭
     */
    @OnClose
    public void onClose() {
        // 从set中删除
        webSocketMap.remove(userId);
        this.offUser(userId);
    }

    /**
     * 发生异常
     */
    @OnError
    public void onErro(Throwable throwable) {
        throwable.printStackTrace();
        webSocketMap.remove(userId);
        this.offUser(userId);
    }

    /**
     * 用户离线
     *
     * @param userId
     */
    public void offUser(String userId) {
        //用户离线
        JsMobileUser jsMobileUser = new JsMobileUser();
        jsMobileUser.setUserCode(userId);
        jsMobileUser.setExtendI1(0L);
        iJsMobileUserService.updateById(jsMobileUser);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("websocket来自客户端的消息:{}", message);
        Message msg = JSONObject.parseObject(message, Message.class);
        if (!"ping".equals(msg.getAction())) {
            //todo 接受到来自客户端的消息
            log.info("websocket来自客户端的消息:{}", message);
        } else {
            log.debug("心跳链接:" + msg.toString());
        }
    }

    /**
     * 获取session
     *
     * @return
     */
    public Session getSession() {
        return session;
    }

    /**
     * 指定发消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            webSocketMap.get(this.userId).getSession().getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定发消息
     *
     * @param message
     */
    public void sendMessage(String senderId, String message) {
        try {
            webSocketMap.get(senderId).getSession().getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param message
     */
    public void fanoutMessage(String message) {
        //遍历Map
        for (Map.Entry<String, WebsocketHandle> entry : webSocketMap.entrySet()) {
            try {
                entry.getValue().getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 群发消息
     *
     * @param message
     */
    public void fanoutMessage(Message message) {
        //遍历Map
        for (Map.Entry<String, WebsocketHandle> entry : webSocketMap.entrySet()) {
            try {
                entry.getValue().getSession().getBasicRemote().sendText(JSONObject.toJSONString(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
