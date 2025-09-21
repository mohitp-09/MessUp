package com.messUp.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class PublicKeyRequest {
    private String username;
    private Map<String, Object> publicKeyJwk;
}
