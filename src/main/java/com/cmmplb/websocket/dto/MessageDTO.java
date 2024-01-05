package com.cmmplb.websocket.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */
@Data
public class MessageDTO {

    /**
     * 消息id
     */
    private Long id;

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
     * 发送时间
     */
    private Date createTime = new Date();

    /**
     * 发送时间:刚刚、今天、日期
     */
    private String time;
}
