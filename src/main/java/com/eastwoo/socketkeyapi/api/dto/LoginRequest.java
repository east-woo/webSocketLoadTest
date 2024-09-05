package com.eastwoo.socketkeyapi.api.dto;

import lombok.Data;

/**
 *packageName    : com.eastwoo.socketkeyapi.api.dto
 * fileName       : LoginRequest
 * author         : dongwoo
 * date           : 2024-09-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-05        dongwoo       최초 생성
 */
@Data
public class LoginRequest {
    private String id;
    private String pw;
    private int keepAliveTimeOut = 900;  // default to 900 seconds (15 minutes)
}