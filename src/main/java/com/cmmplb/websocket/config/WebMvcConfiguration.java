package com.cmmplb.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author penglibo
 * @date 2021-09-19 22:57:43
 * @since jdk 1.8
 */

@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 配置页面请求路径
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }
}
