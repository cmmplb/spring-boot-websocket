package com.cmmplb.websocket.vo;

import com.cmmplb.websocket.utils.SecurityUtil;
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
public class ContactMessageVO implements Serializable {

    /**
     * 消息记录id
     */
    private Long id;

    /**
     * 头像-图片访问地址
     */
    private String avatar;

    /**
     * 类型:1-用户;2-群
     */
    private Byte type;

    /**
     * 1-发送;2-接收;
     */
    private Byte send;

    /**
     * 发送人:用户id/群id
     */
    private Long sendBusinessId;

    /**
     * 消息
     */
    private String message;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Byte getSend() {
        if (this.type.equals((byte) 1)) {
            if (SecurityUtil.getUserId().equals(sendBusinessId)) {
                return (byte) 1;
            } else {
                return (byte) 2;
            }
        }
        return (byte) 2;
    }
}
