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
public class MessageRecordPageQueryDTO extends QueryPageBean {

    /**
     * 用户id/群id
     */
    private String businessId;

}
