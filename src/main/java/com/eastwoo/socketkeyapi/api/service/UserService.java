package com.eastwoo.socketkeyapi.api.service;

import com.eastwoo.socketkeyapi.api.dto.LoginRequest;
import com.eastwoo.socketkeyapi.api.dto.LoginResponse;
import com.eastwoo.socketkeyapi.api.model.User;
import com.eastwoo.socketkeyapi.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;
import java.util.UUID;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.service
 * fileName       : UserService
 * author         : dongwoo
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        dongwoo       최초 생성
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Value("${keepAlive.default.timeout}")
    private int defaultKeepAliveTimeout;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameAndPassword(loginRequest.getId(), loginRequest.getPw());

        if (user != null) {
            String apiKey = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());

            int keepAliveTimeOut = loginRequest.getKeepAliveTimeOut() > 0
                    ? loginRequest.getKeepAliveTimeOut()
                    : defaultKeepAliveTimeout;

            return new LoginResponse(apiKey);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}