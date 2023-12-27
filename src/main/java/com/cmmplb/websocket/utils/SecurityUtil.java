package com.cmmplb.websocket.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author penglibo
 * @date 2023-11-16 11:24:42
 * @since jdk 1.8
 */
public class SecurityUtil {

    public static Long getUserId() {
        // 这里为了简化，用户是前端请求头传的用户id，业务中要接入用户逻辑，比如token凭证获取用户信息逻辑
        String userId = getRequest().getHeader("User-Id");
        return Long.parseLong(userId);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

}
