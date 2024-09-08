package com.eastwoo.socketkeyapi.api.user.controller;

import com.eastwoo.socketkeyapi.api.user.dto.ApiResponse;
import com.eastwoo.socketkeyapi.api.user.model.ApiResponseStatus;
import com.eastwoo.socketkeyapi.api.user.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.user.controller
 * fileName       : KeepAliveController
 * author         : dongwoo
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        dongwoo       최초 생성
 */
@RestController
@RequestMapping("/keepalive")
public class KeepAliveController {

    private final ApiKeyService apiKeyService;

    public KeepAliveController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PutMapping
    public ResponseEntity<?> keepAlive(@RequestHeader("api-key") String apiKey) {
        boolean updated = apiKeyService.validateApiKey(apiKey);

        if (updated) {
            apiKeyService.extendApiKeyExpiration(apiKey);
            return ResponseEntity.ok(new ApiResponse(ApiResponseStatus.OK.getMessage()));
        } else {
            return ResponseEntity.status(ApiResponseStatus.INVALID_API_KEY.getStatusCode())
                    .body(new ApiResponse(ApiResponseStatus.INVALID_API_KEY.getMessage()));
        }
    }
}