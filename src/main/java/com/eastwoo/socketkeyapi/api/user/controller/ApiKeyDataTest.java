package com.eastwoo.socketkeyapi.api.user.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;

public class ApiKeyDataTest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ApiKeyData {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime expiresAt;
        private String userId;

        @Override
        public String toString() {
            return "ApiKeyData{" +
                    "expiresAt=" + expiresAt +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ApiKeyData original = ApiKeyData.builder()
                .expiresAt(LocalDateTime.now())
                .userId("user123")
                .build();

        // Serialize
        String json = objectMapper.writeValueAsString(original);
        System.out.println("Serialized JSON: " + json);

        // Deserialize
        ApiKeyData deserialized = objectMapper.readValue(json, ApiKeyData.class);
        System.out.println("Deserialized Object: " + deserialized);
    }
}