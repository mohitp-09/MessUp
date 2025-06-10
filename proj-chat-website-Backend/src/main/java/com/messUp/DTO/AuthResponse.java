package com.messUp.DTO;

import lombok.Data;

@Data
public class AuthResponse {
    String token;
    String username;

    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
