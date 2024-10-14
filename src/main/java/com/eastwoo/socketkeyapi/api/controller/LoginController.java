package com.eastwoo.socketkeyapi.api.controller;

import com.eastwoo.socketkeyapi.api.dto.LoginRequest;
import com.eastwoo.socketkeyapi.api.dto.LoginResponse;
import com.eastwoo.socketkeyapi.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
@RestController
@RequestMapping("/users")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest);
        return userService.login(loginRequest);
    }
}