package com.messUp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messUp.DTO.PublicKeyRequest;
import com.messUp.entity.PublicKey;
import com.messUp.entity.User;
import com.messUp.repository.UserPublicKeyRepository;
import com.messUp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keys")
public class KeyExchangeController {

    private final UserPublicKeyRepository userPublicKeyRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public KeyExchangeController(UserPublicKeyRepository userPublicKeyRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.userPublicKeyRepository = userPublicKeyRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPublicKey(@RequestBody PublicKeyRequest request) throws JsonProcessingException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
            () -> new RuntimeException("User not found")
        );

        PublicKey key = userPublicKeyRepository.findByUser(user)
            .orElse(new PublicKey());

        key.setUser(user);

        String jwkJson=objectMapper.writeValueAsString(request.getPublicKeyJwk());
        key.setPublicKeyJwk(jwkJson);
        userPublicKeyRepository.save(key);
        return ResponseEntity.ok("Public key uploaded successfully");
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getPublicKey(@PathVariable String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new RuntimeException("User not found")
        );
        PublicKey key = userPublicKeyRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Public key not found"));
        return ResponseEntity.ok(key.getPublicKeyJwk());
    }
}
