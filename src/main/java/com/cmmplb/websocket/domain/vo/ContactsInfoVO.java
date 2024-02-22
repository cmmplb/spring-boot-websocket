package com.cmmplb.websocket.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */

@Data
public class ContactsInfoVO implements Serializable {

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
     * 备注
     */
    private String remark;

    /**
     * 共同群聊
     */
    private Integer count;
}
