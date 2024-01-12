package com.cmmplb.websocket.dto;

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
public class MessageRecordPageQueryDTO extends QueryPageBean {

    /**
     * 类型:1-用户;2-群
     */
    private Byte type;

    /**
     * 用户id/群id
     */
    private Long businessId;

}
