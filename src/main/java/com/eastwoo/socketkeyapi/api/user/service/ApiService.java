package com.eastwoo.socketkeyapi.api.user.service;

import org.springframework.stereotype.Service;

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

    private final StringRedisTemplate redisTemplate;

    @Value("${keepAlive.default.timeout}")
    private int defaultKeepAliveTimeout;

    public ApiKeyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateApiKey(String userId) {
        String apiKey = UUID.randomUUID().toString();
        storeApiKey(apiKey, userId, defaultKeepAliveTimeout);
        return apiKey;
    }

    public void storeApiKey(String apiKey, String userId, int timeout) {
        redisTemplate.opsForValue().set(apiKey, userId, Duration.ofSeconds(timeout));
    }

    public boolean validateApiKey(String apiKey) {
        return redisTemplate.hasKey(apiKey);
    }

    public void extendApiKeyExpiration(String apiKey, int timeout) {
        if (redisTemplate.hasKey(apiKey)) {
            redisTemplate.expire(apiKey, Duration.ofSeconds(timeout));
        }
    }

    public Map<Object, Object> getApiKeyInfo(String apiKey) {
        return redisTemplate.opsForHash().entries(apiKey);
    }
}