package com.cmmplb.websocket.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;

/**
 * @author plb
 * @date 2021-01-06
 * Websocket基于tomcat实现
 * Component默认是单例模式的，但spring还是会为每个websocket连接初始化一个端点bean
 * 注意的点：aop代理会导致as it is not annotated with @ServerEndpoint
 */

@Slf4j
@Component
@javax.websocket.server.ServerEndpoint(value = "/server")
public class ServerEndpoint {

    public static String applicationName;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("连接建立成功调用的方法");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        log.info("连接关闭调用的方法");
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误时调用");
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        String id = session.getId();
        System.out.println(id);
        System.out.println(session);
        log.info("接收消息");
        log.info(message);
    }

    /**
     * 封装一个send方法，发送消息到前端
     */
    private void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("发生错误时调用");
        }
    }
}
