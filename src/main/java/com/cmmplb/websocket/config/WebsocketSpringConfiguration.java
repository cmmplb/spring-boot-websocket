package com.cmmplb.websocket.config;

import com.cmmplb.websocket.handler.ChatTextWebSocketHandler;
import com.cmmplb.websocket.interceptor.WebsocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author penglibo
 * @date 2023-12-18 10:50:32
 * @since jdk 1.8
 * Websocket基于spring实现
 */

@Configuration
@EnableWebSocket
public class WebsocketSpringConfiguration implements WebSocketConfigurer {

    private static final String CHAT = "/chat";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatTextWebSocketHandler(), CHAT)
                // 参数处理
                .addInterceptors(new WebsocketInterceptor())
                // session握手拦截器
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                // 关闭跨域校验
                .setAllowedOrigins("*");
    }

    @Bean
    public ChatTextWebSocketHandler chatTextWebSocketHandler() {
        return new ChatTextWebSocketHandler();
    }
}
