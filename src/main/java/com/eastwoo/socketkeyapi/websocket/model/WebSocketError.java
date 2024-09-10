package com.eastwoo.socketkeyapi.websocket.model;

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
        return String.format("{\"ErrorCode\": %d, \"msg\": \"%s\"}", errorCode, message);
    }
}