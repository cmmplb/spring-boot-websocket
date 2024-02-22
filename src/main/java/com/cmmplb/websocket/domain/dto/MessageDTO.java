package com.cmmplb.websocket.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.cmmplb.websocket.constants.GlobalConstants;
import com.cmmplb.websocket.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */
@Data
@NoArgsConstructor
public class MessageDTO implements Serializable {

    /**
     * 消息类型:1-心跳;2-聊天;3-更新单条消息状态;4-更新所有消息状态
     */
    private Byte type;

    /**
     * 消息
     */
    private Message message;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {

        /**
         * 消息id
         */
        private Long id;

        /**
         * 发送人姓名
         */
        private String name;

        /**
         * 发送人头像
         */
        private String avatar;

        /**
         * 业务id:type-用户id/群id
         */
        private String businessId;

        /**
         * 发送人:用户id/群id
         */
        private Long sendBusinessId;

        /**
         * 接收人:用户id/群id
         */
        private Long receiveBusinessId;

        /**
         * 类型:1-用户;2-群
         */
        private Byte type;

        /**
         * 发送的文本
         */
        private String message;

        /**
         * uuid
         */
        private String uuid;

        /**
         * 发送时间
         */
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private final Date sendTime = new Date();

        /**
         * 发送时间:刚刚、今天、日期
         */
        private String time;

        /**
         * 最近消息时间
         */
        private String recentlyTime;

        public String getTime() {
            return DateUtil.dateFormat(sendTime, GlobalConstants.NUM_TWO);
        }

        public String getRecentlyTime() {
            return DateUtil.dateFormat(sendTime, GlobalConstants.NUM_THREE);
        }

        public Message(String uuid, String businessId) {
            this.uuid = uuid;
            this.businessId = businessId;
        }

        public Message(String businessId) {
            this.businessId = businessId;
        }
    }

    public MessageDTO(Byte type) {
        this.type = type;
    }

    public MessageDTO(Byte type, Message message) {
        this.type = type;
        this.message = message;
    }
}
