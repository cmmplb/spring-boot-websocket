package com.cmmplb.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cmmplb.websocket.dto.MessageDTO;
import com.cmmplb.websocket.entity.MessageRecord;
import com.cmmplb.websocket.entity.RecentlyMessage;
import com.cmmplb.websocket.service.MessageRecordService;
import com.cmmplb.websocket.service.RecentlyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author plb
 * @date 2022-03-01
 * Websocket基于spring实现
 */

@Slf4j
public class ChatTextWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private MessageRecordService messageRecordService;

    @Autowired
    private RecentlyMessageService recentlyMessageService;

    /**
     * key：用户id
     * value：连接session
     */
    public static final Map<Long, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        log.info("afterConnectionEstablished");
        Object userId = attributes.get("userId");
        if (null != userId) {
            SESSION_MAP.put(Long.parseLong(userId.toString()), session);
        }
    }

    /**
     * 断开连接调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        log.info("attributes:{}", attributes);
        super.afterConnectionClosed(session, status);
        log.info("afterConnectionClosed");
        Object userId = attributes.get("userId");
        if (null != userId) {
            SESSION_MAP.remove(Long.parseLong(userId.toString()));
        }
    }

    /**
     * 接收消息时调用
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage");
        log.info("收到客户端消息：{}", message.getPayload());
        Map<String, Object> attributes = session.getAttributes();
        Long userId = Long.parseLong(attributes.get("userId").toString());
        log.info("attributes:{}", attributes);
        MessageDTO msg = JSONObject.parseObject(message.getPayload(), new TypeReference<MessageDTO>() {
        });

        // todo:这里可以做一个临时存储，数据量到达n条或者用户断开连接以后才保存，避免每次发送都调用数据库保存
        // 保存消息记录
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setType(msg.getType());
        messageRecord.setSendBusinessId(userId);
        messageRecord.setReceiveBusinessId(msg.getReceiveBusinessId());
        messageRecord.setMessage(msg.getMessage());
        messageRecord.setSendTime(new Date());
        messageRecord.setCreateTime(new Date());
        messageRecord.setCreateBy(userId);
        messageRecord.setUpdateTime(new Date());
        messageRecord.setUpdateBy(userId);
        messageRecordService.save(messageRecord);

        // 保存最近消息
        RecentlyMessage recentlyMessage = recentlyMessageService.getOne(new LambdaQueryWrapper<RecentlyMessage>()
                .eq(RecentlyMessage::getSendBusinessId, userId)
                .eq(RecentlyMessage::getReceiveBusinessId, msg.getReceiveBusinessId())
        );
        if (null == recentlyMessage) {
            recentlyMessage = new RecentlyMessage();
            recentlyMessage.setType(msg.getType());
            recentlyMessage.setSendBusinessId(userId);
            recentlyMessage.setReceiveBusinessId(msg.getReceiveBusinessId());
            recentlyMessage.setMessage(msg.getMessage());
            recentlyMessage.setSendTime(new Date());
            recentlyMessage.setCreateTime(new Date());
            recentlyMessage.setCreateBy(userId);
            recentlyMessage.setUpdateTime(new Date());
            recentlyMessage.setUpdateBy(userId);
            recentlyMessageService.save(recentlyMessage);
        } else {
            recentlyMessage.setSendTime(new Date());
            recentlyMessage.setMessage(msg.getMessage());
            recentlyMessage.setUpdateTime(new Date());
            recentlyMessage.setUpdateBy(userId);
            recentlyMessageService.updateById(recentlyMessage);
        }
        msg.setId(messageRecord.getId());
        log.info("msg：{}", msg);
        sendMessage(msg);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info("handleBinaryMessage");
        super.handleBinaryMessage(session, message);
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

    /**
     * 发送消息
     */
    public void sendMessage(MessageDTO dto) throws IOException {
        if (!CollectionUtils.isEmpty(SESSION_MAP)) {
            WebSocketSession webSocketSession = SESSION_MAP.get(dto.getReceiveBusinessId());
            TextMessage msg = new TextMessage(JSON.toJSONString(dto));
            if (null != webSocketSession) {
                webSocketSession.sendMessage(msg);
            }
        }
    }
}
