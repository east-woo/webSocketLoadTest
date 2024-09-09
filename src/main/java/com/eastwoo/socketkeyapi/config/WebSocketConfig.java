package com.eastwoo.socketkeyapi.config;


import com.eastwoo.socketkeyapi.websocket.handeler.WebSocketHandler;
import com.eastwoo.socketkeyapi.websocket.interceptor.ApiKeyHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : WebSocketConfig
 * @since : 2024-09-09
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws")
                .addInterceptors(new ApiKeyHandshakeInterceptor())
                .setAllowedOrigins("*"); // Customize allowed origins for security
    }
}