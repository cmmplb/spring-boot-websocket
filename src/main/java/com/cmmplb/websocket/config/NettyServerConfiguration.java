package com.cmmplb.websocket.config;

import com.cmmplb.websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author penglibo
 * @date 2021-07-10 10:58:49
 * @since jdk 1.8
 */

@Component
public class NettyServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerConfiguration.class);

    @PostConstruct
    public void init() {
        logger.info("启动webSocket服务器");
        // 81 netty
        new WebSocketServer(81).start(); // websocket使用spring容器启动
    }
}
