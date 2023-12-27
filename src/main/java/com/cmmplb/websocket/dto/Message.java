package com.cmmplb.websocket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Message {

    /**
     * 发送者name
     */
    public String from;

    /**
     * 接收者name
     */
    public String to;

    /**
     * 发送的文本
     */
    public String text;

    /**
     * 发送时间
     */
    public Date date;
}
