package com.cmmplb.websocket.domain.dto;

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
public class RecentlyMessageDTO implements Serializable {

    /**
     * 接收人:用户id/群id
     */
    private Long receiveBusinessId;

    /**
     * 消息
     */
    private String message;

    /**
     * 发送时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
}
