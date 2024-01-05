package com.cmmplb.websocket.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */

@Data
public class ContactVO implements Serializable {

    /**
     * 联系人id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像-图片访问地址
     */
    private String avatar;

    /**
     * 类型:1-用户;2-群
     */
    private Byte type;
}
