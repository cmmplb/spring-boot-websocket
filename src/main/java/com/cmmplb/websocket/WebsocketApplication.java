package com.cmmplb.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author plb
 * @date 2021-01-06
 */

@Slf4j
@SpringBootApplication
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(WebsocketApplication.class);
        ConfigurableApplicationContext context = builder.build().run(args);
        Environment env = context.getEnvironment();
        String serverPort = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t\thttp://localhost:{}\n\t" +
                        "Profile(s): {}\n----------------------------------------------------------"
                , env.getProperty("spring.application.name"), serverPort, env.getActiveProfiles()
        );
    }
}