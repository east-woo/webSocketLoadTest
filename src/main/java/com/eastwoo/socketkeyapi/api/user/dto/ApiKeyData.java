package com.eastwoo.socketkeyapi.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.user.dto
 * fileName       : ApiKey
 * author         : dongwoo
 * date           : 2024-09-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        dongwoo       최초 생성
 */
@Getter
@Builder
public class ApiKeyData {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;
    private String userId;
}
