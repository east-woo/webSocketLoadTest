package com.eastwoo.socketkeyapi.api.dto;

import lombok.Getter;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.user.dto
 * fileName       : ApiResponse
 * author         : dongwoo
 * date           : 2024-09-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        dongwoo       최초 생성
 */
@Getter
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }
}
