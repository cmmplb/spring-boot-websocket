package com.cmmplb.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * @author plb
 * @date 2022-03-01
 * Websocket基于spring实现
 */

@Slf4j
public class ChatTextWebSocketHandler extends TextWebSocketHandler {

    /**
     * 断开连接调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        super.afterConnectionClosed(session, status);
        log.info("afterConnectionClosed");
    }

    /**
     * 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        log.info("afterConnectionEstablished");
    }

    /**
     * 接收消息时调用
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage");
        TextMessage msg = new TextMessage(message.getPayload());
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        session.sendMessage(msg);
    }

    /**
     * 接收消息时调用
     */
    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) throws Exception {
        log.info("handleMessage");
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        super.handleMessage(session, message);
    }

    /**
     * 异常时调用
     */
    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        log.info("handleTransportError");
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        super.handleTransportError(session, exception);
    }
}
