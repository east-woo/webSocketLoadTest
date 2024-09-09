package com.eastwoo.socketkeyapi.websocket.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * Please explain the class!!
 *
 * @fileName      : ApiKeyHandshakeInterceptor
 * @author        : dongwoo
 * @since         : 2024-09-09
 */
public class ApiKeyHandshakeInterceptor extends HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String apiKey = request.getParameter("api-key");

        // Validate the API key
        if (isValidApiKey(apiKey)) {
            return true; // Continue with the handshake
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false; // Terminate the handshake
        }
    }

    private boolean isValidApiKey(String apiKey) {
        return "valid-api-key".equals(apiKey); // Example condition
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}