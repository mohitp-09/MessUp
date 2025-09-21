package com.messUp.DTO;

import lombok.Data;

@Data
public class AuthResponse {
    String token;
    String refreshToken;
    String username;

    public AuthResponse(String token, String username, String refreshToken) {
        this.token = token;
        this.username = username;
        this.refreshToken = refreshToken;
    }

    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
