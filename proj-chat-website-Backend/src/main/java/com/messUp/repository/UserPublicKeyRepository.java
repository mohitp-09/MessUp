package com.messUp.repository;

import com.messUp.entity.PublicKey;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPublicKeyRepository extends JpaRepository<PublicKey, Long> {

    Optional<PublicKey> findByUser(User user);
}
