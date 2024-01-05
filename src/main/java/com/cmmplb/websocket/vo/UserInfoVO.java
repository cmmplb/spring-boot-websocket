package com.cmmplb.websocket.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2021-08-17 15:58:38
 * @since jdk 1.8
 */

@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = -5029097111709615443L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 头像-图片访问地址
     */
    private String avatar;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别:0-女;1-男;2-未知;
     */
    private Byte sex;

    /**
     * 用户状态:0-正常;1-禁用
     */
    private Byte status;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
