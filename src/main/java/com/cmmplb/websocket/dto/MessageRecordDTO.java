package com.cmmplb.websocket.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */

@Data
public class MessageRecordDTO implements Serializable {

    /**
     * 消息记录id
     */
    private Long id;

    /**
     * 消息
     */
    private String message;

    /**
     * 创建时间
     */
    private Date createTime;
}
