package com.messUp.controller;

import com.messUp.DTO.AuthResponse;
import com.messUp.DTO.LoginRequest;
import com.messUp.DTO.RegisterRequest;
import com.messUp.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(LoginRequest request) {
        return authService.login(request);
    }
}
