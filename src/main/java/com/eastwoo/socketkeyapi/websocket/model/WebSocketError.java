package com.eastwoo.socketkeyapi.websocket.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : WebSocketError
 * @since : 2024-09-10
 */
public enum WebSocketError {
    INVALID_API_KEY(501, "Invalid API-KEY");

    private final int errorCode;
    private final String message;

    WebSocketError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // JSON 변환 오류 시 빈 JSON 객체 반환
        }
    }
}