package com.cmmplb.websocket.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author penglibo
 * @date 2023-11-17 09:44:23
 * @since jdk 1.8
 */

@Data
public class AttachmentVO {

    /**
     * id
     */
    private Long id;

    /**
     * 附件名称
     */
    private String name;

    /**
     * 文件md5
     */
    private String md5;

    /**
     * 附件类型：如.docx/.zip等
     */
    private String type;

    /**
     * 附件大小
     */
    private Long size;

    /**
     * 附件资源访问地址
     */
    private String url;

}
