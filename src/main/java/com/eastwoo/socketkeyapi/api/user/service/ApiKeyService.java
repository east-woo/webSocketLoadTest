package com.eastwoo.socketkeyapi.api.user.service;

import com.eastwoo.socketkeyapi.api.user.dto.ApiKeyData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.user.service
 * fileName       : ApiService
 * author         : dongwoo
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        dongwoo       최초 생성
 */
@Service
public class ApiKeyService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${keepAlive.default.timeout}")
    private int defaultKeepAliveTimeout;


    public ApiKeyService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 새로운 API 키를 생성하고 저장
     *
     * @param userId 사용자 ID
     * @return 생성된 API 키
     */
    public String generateApiKey(String apiKey, String userId) {
        storeApiKey(apiKey, userId, defaultKeepAliveTimeout);
        return apiKey;
    }

    /**
     * API 키와 관련된 정보를 Redis에 저장
     *
     * @param apiKey API 키
     * @param userId 사용자 ID
     * @param timeout 유효 기간(초)
     */
    public void storeApiKey(String apiKey, String userId, int timeout) {
        ApiKeyData apiKeyData = ApiKeyData.builder()
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusSeconds(timeout))
                .build();
        redisTemplate.opsForValue().set(apiKey, apiKeyData, Duration.ofSeconds(timeout));
    }

    /**
     * API 키의 유효성을 검사
     *
     * @param apiKey API 키
     * @return 유효한 경우 true, 그렇지 않으면 false
     */
    public boolean validateApiKey(String apiKey) {
        return redisTemplate.hasKey(apiKey);
    }

    /**
     * API 키의 유효 기간을 연장
     *
     * @param apiKey API 키
     * @param timeout 연장할 유효 기간(초)
     */
    private void extendApiKeyExpiration(String apiKey, int timeout) {
        if (redisTemplate.hasKey(apiKey)) {
            ApiKeyData apiKeyData = (ApiKeyData) redisTemplate.opsForValue().get(apiKey);
            if (apiKeyData != null) {
                apiKeyData = ApiKeyData.builder()
                        .userId(apiKeyData.getUserId())
                        .expiresAt(LocalDateTime.now().plusSeconds(timeout))
                        .build();
                redisTemplate.opsForValue().set(apiKey, apiKeyData, Duration.ofSeconds(timeout));
            }
        }
    }

    /**
     * API 키에 대한 정보를 반환
     *
     * @param apiKey API 키
     * @return API 키 정보
     */
    public ApiKeyData getApiKeyInfo(String apiKey) {
        return (ApiKeyData) redisTemplate.opsForValue().get(apiKey);
    }

    public boolean extendApiKeyExpiry(String apiKey) {
        this.extendApiKeyExpiration(apiKey, defaultKeepAliveTimeout);

    }
}