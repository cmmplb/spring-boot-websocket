package com.cmmplb.websocket.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author plb
 * @date 2020/6/15 11:16
 * 附件表
 */

@Data
@TableName(value = "sys_attachment")
public class Attachment implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 附件名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 文件md5
     */
    @TableField(value = "md5")
    private String md5;

    /**
     * 附件类型：如.docx/.zip等
     */
    @TableField(value = "type")
    private String type;

    /**
     * 附件大小
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 附件资源访问地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
