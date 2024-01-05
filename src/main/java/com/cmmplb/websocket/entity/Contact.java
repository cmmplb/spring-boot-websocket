package com.cmmplb.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author plb
 * @date 2020/6/15 11:16
 * 联系人表
 */
@Data
@TableName(value = "chat_contact")
public class Contact implements Serializable {

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
     * 所属用户id
     */
    private Long userId;

    /**
     * 用户id/群id
     */
    private Long businessId;

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