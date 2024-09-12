package com.eastwoo.socketkeyapi.websocket.handeler;


import com.eastwoo.socketkeyapi.api.service.ApiKeyService;
import com.eastwoo.socketkeyapi.websocket.model.WebSocketError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : WebSocketHandler
 * @since : 2024-09-09
 */
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ApiKeyService apiKeyService;

    public WebSocketHandler(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String apiKey = (String) session.getAttributes().get("api-key");

        if (apiKey == null || !apiKeyService.hasApiKey(apiKey)) {
            session.sendMessage(new TextMessage(WebSocketError.INVALID_API_KEY.toJson()));
            session.close(CloseStatus.NORMAL.withReason(WebSocketError.INVALID_API_KEY.getMessage()));
            return;
        }
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void broadcastMessage(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            String apiKey = (String) session.getAttributes().get("api-key");

            if (session.isOpen() && apiKey != null && apiKeyService.hasApiKey(apiKey)) {
                session.sendMessage(new TextMessage(message));
            } else {
                session.sendMessage(new TextMessage(WebSocketError.INVALID_API_KEY.toJson()));
                session.close(CloseStatus.NORMAL.withReason(WebSocketError.INVALID_API_KEY.getMessage()));

            }
        }
    }
}