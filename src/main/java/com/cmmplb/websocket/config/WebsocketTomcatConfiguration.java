package com.cmmplb.websocket.config;

import com.cmmplb.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author penglibo
 * @date 2023-12-18 10:50:32
 * @since jdk 1.8
 * Websocket基于tomcat实现
 */

@Configuration
public class WebsocketTomcatConfiguration {

    /**
     * 扫描并注册所有携带@ServerEndpoint注解的实例。 @ServerEndpoint("/websocket")
     * 如果使用外部容器，不是直接使用springboot的内置容器，则无需提供ServerEndpointExporter。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 解决ServerEndpoint类上不能直接注入
     */
    @Value("${spring.application.name}")
    public void setMsgSessionService(String applicationName) {
        ServerEndpoint.applicationName = applicationName;
    }
}
