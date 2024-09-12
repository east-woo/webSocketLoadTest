package com.eastwoo.socketkeyapi.websocket.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.ErrorResponse;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : WebSocketError
 * @since : 2024-09-10
 */
@Getter
@AllArgsConstructor
public enum WebSocketError {
    INVALID_API_KEY(501, "Invalid API-KEY");

    private final int errorCode;
    private final String message;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String toJson() {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        try {
            return OBJECT_MAPPER.writeValueAsString(errorResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorResponse {
        private final int ErrorCode;
        private final String msg;
    }
}