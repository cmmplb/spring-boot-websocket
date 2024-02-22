package com.cmmplb.websocket.domain.vo;

import com.cmmplb.websocket.constants.GlobalConstants;
import com.cmmplb.websocket.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penglibo
 * @date 2023-12-29 10:14:18
 * @since jdk 1.8
 */

@Data
public class RecentlyMessageVO implements Serializable {

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

    /**
     * 1-发送;2-接收;
     */
    private Byte send;

    /**
     * 业务id:type-用户id/群id
     */
    private String businessId;

    /**
     * 消息
     */
    private String message;

    /**
     * 发送时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 消息uuid
     */
    private String uuid;

    /**
     * 未读消息数量;
     */
    private Integer count;

    /**
     * 发送时间:刚刚、今天、日期
     */
    private String time;

    /**
     * 最近消息时间
     */
    private String recentlyTime;

    public String getTime() {
        if (null != sendTime) {
            return DateUtil.dateFormat(sendTime, GlobalConstants.NUM_TWO);
        }
        return time;
    }

    public String getRecentlyTime() {
        return DateUtil.dateFormat(sendTime, GlobalConstants.NUM_THREE);
    }

}
