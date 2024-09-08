package com.eastwoo.socketkeyapi.api.user.model;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.user.model
 * fileName       : ApiResponseStatus
 * author         : dongwoo
 * date           : 2024-09-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        dongwoo       최초 생성
 */
public enum ApiResponseStatus {
    OK(200, "ok"),
    INVALID_API_KEY(452, "check your api-key");

    private final int statusCode;
    private final String message;

    ApiResponseStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
