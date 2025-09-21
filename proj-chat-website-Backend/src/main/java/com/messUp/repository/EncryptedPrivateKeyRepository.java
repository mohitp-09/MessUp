package com.messUp.repository;

import com.messUp.entity.EncryptedPrivateKey;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EncryptedPrivateKeyRepository extends JpaRepository<EncryptedPrivateKey, Long> {

    Optional<EncryptedPrivateKey> findByUser(User user);

    // Additional methods can be defined here if needed
}
