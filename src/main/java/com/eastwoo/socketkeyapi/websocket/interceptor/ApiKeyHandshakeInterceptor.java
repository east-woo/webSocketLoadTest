package com.eastwoo.socketkeyapi.websocket.interceptor;

import com.eastwoo.socketkeyapi.api.service.ApiKeyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
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
@Component
public class ApiKeyHandshakeInterceptor extends HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    private final ApiKeyService apiKeyService;

    public ApiKeyHandshakeInterceptor(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // ServerHttpRequest를 ServletServerHttpRequest로 캐스팅하여 HttpServletRequest를 얻습니다.
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            // 요청에서 API 키를 쿼리 파라미터로 가져옵니다.
            String apiKey = httpRequest.getParameter("api-key");

            // API 키를 검증합니다.
            if (apiKeyService.hasApiKey(apiKey)) {
                return true;
            } else {
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }
        }

        response.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}