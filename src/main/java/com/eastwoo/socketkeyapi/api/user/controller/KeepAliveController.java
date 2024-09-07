package com.eastwoo.socketkeyapi.api.user.controller;

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
        if (apiKeyService.validateApiKey(apiKey)) {
            apiKeyService.extendApiKeyExpiration(apiKey, 900); // Extend by 15 minutes
            return ResponseEntity.ok(Map.of("msg", "ok"));
        } else {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(Map.of("msg", "check your api-key"));
        }
    }
}