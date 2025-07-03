package com.messUp.DTO;

import lombok.Data;

@Data
public class EncryptedKeyRequest {
    private String username;
    private String encryptedPrivateKey;
    private String salt;
    private String iv;
}
