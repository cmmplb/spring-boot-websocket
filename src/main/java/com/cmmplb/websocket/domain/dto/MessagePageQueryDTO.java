package com.cmmplb.websocket.domain.dto;

import com.cmmplb.websocket.beans.QueryPageBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessagePageQueryDTO extends QueryPageBean {

    /**
     * 类型:1-用户;2-群
     */
    private Byte type;

    /**
     * 发送人用户id
     */
    private Long sendBusinessId;

}
