package com.eastwoo.socketkeyapi.api.controller;

import com.eastwoo.socketkeyapi.api.dto.LoginRequest;
import com.eastwoo.socketkeyapi.api.dto.LoginResponse;
import com.eastwoo.socketkeyapi.api.model.User;
import com.eastwoo.socketkeyapi.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.controller
 * fileName       : LoginController
 * author         : dongwoo
 * date           : 2024-09-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-05        dongwoo       최초 생성
 */
@RestController
@RequestMapping("/users")
public class LoginController {

    private final UserRepository userRepository;

    @Value("${keepAlive.default.timeout}")
    private int defaultKeepAliveTimeout;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
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