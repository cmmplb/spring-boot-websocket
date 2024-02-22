package com.cmmplb.websocket.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */

@Data
public class MessageRecordVO implements Serializable {

    /**
     * 消息记录id
     */
    private Long id;

    /**
     * 类型:1-用户;2-群
     */
    private Byte type;

    /**
     * 1-发送;2-接收;
     */
    private Byte send;

    /**
     * 业务id:type-用户id/群id
     */
    private String businessId;

    /**
     * 消息
     */
    private String message;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像-图片访问地址
     */
    private String avatar;

    /**
     * 发送时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 状态:0-未读;1-已读;
     */
    private Byte status;

    /**
     * uuid
     */
    private String uuid;
}
