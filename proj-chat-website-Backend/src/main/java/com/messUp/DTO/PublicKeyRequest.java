package com.messUp.DTO;

import lombok.Data;

@Data
public class PublicKeyRequest {
    private String username;
    private String publicKeyJwk;
}
