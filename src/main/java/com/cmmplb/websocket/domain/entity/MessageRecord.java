package com.cmmplb.websocket.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author plb
 * @date 2020/6/15 11:16
 * 消息记录表
 */
@Data
@TableName(value = "chat_message_record")
public class MessageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型:1-用户;2-群
     */
    private Byte type;

    /**
     * 发送人:用户id/群id
     */
    private Long sendBusinessId;

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
    private Date sendTime;

    /**
     * 状态:0-未读;1-已读;
     */
    private Byte status;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateBy;
}