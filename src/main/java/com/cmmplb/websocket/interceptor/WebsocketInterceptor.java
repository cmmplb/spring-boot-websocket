package com.cmmplb.websocket.interceptor;

import com.cmmplb.websocket.utils.MapObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author penglibo
 * @date 2023-12-18 14:36:20
 * @since jdk 1.8
 */

@Slf4j
public class WebsocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler webSocketHandler,
                                   @NonNull Map<String, Object> attributes) {
        log.info("握手开始");
        Map<String, String> paramMap = MapObjectUtil.urlParams2Map(request.getURI().getQuery());
        // 将路径请求参数保存到attributes，在ChatTextWebSocketHandler中session.getAttributes()获取
        if (!CollectionUtils.isEmpty(paramMap)) {
            attributes.putAll(paramMap);
        }
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
                               @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler webSocketHandler,
                               Exception e) {
        log.info("握手完成");
    }
}
