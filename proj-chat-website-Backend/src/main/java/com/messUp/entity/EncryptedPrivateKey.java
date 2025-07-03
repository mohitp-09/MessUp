package com.messUp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_encrypted_keys")
public class EncryptedPrivateKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String encryptedPrivateKeyJwk;

    @Column(nullable = false)
    private String salt;

    @Column(length = 64)
    private String iv;

}
