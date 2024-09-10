package com.eastwoo.socketkeyapi.websocket.interceptor;

import com.eastwoo.socketkeyapi.api.service.ApiKeyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import reactor.core.publisher.Mono;

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
        HttpServletRequest httpRequest = getHttpServletRequest(request);
        if (httpRequest == null) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
        return true;
/*        String apiKey = httpRequest.getParameter("api-key");

        // API 키를 검증
        if (apiKeyService.hasApiKey(apiKey)) {
            return true;
        } else {
            response.setStatusCode(HttpStatus.NOT_IMPLEMENTED);
            return false;
        }*/


    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 이후 작업 처리 가능
    }

    // HttpServletRequest를 추출하는 메서드를 별도로 만들어 다형성을 활용
    private HttpServletRequest getHttpServletRequest(ServerHttpRequest request) {
        // 다양한 ServerHttpRequest 구현체에 맞는 로직을 추가 가능
        if (request instanceof ServletServerHttpRequest) {
            return ((ServletServerHttpRequest) request).getServletRequest();
        }
        // 추후 확장이 필요할 경우 추가 구현체에 맞는 처리 추가 가능
        return null;
    }

}