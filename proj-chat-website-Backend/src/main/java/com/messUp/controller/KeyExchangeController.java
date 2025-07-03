package com.messUp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messUp.DTO.EncryptedKeyRequest;
import com.messUp.DTO.PublicKeyRequest;
import com.messUp.entity.EncryptedPrivateKey;
import com.messUp.entity.PublicKey;
import com.messUp.entity.User;
import com.messUp.repository.EncryptedPrivateKeyRepository;
import com.messUp.repository.UserPublicKeyRepository;
import com.messUp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/keys")
public class KeyExchangeController {

    private final UserPublicKeyRepository userPublicKeyRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final EncryptedPrivateKeyRepository encryptedPrivateKeyRepo;

    public KeyExchangeController(UserPublicKeyRepository userPublicKeyRepository, UserRepository userRepository, ObjectMapper objectMapper,
                                 EncryptedPrivateKeyRepository encryptedPrivateKeyRepo) {
        this.userPublicKeyRepository = userPublicKeyRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.encryptedPrivateKeyRepo = encryptedPrivateKeyRepo;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPublicKey(@RequestBody PublicKeyRequest request) throws JsonProcessingException {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Optional<PublicKey> existing = userPublicKeyRepository.findByUser(user);
            PublicKey key = existing.orElseGet(() -> {
                PublicKey newKey = new PublicKey();
                newKey.setUser(user);
                return newKey;
            });

            String jwkJson = objectMapper.writeValueAsString(request.getPublicKeyJwk());
            key.setPublicKeyJwk(jwkJson);
            userPublicKeyRepository.save(key);
            return ResponseEntity.ok("Public key uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Failed to upload public key", "message", e.getMessage()));
        }
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getPublicKey(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            PublicKey key = userPublicKeyRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Public key not found"));

            return ResponseEntity.ok(key.getPublicKeyJwk());

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("error", "Key not found", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Unexpected error", "message", e.getMessage()));
        }
    }

    @PostMapping("/upload-private")
    public ResponseEntity<?> uploadEncryptedPrivateKey(@RequestBody EncryptedKeyRequest request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            EncryptedPrivateKey existing = encryptedPrivateKeyRepo.findByUser(user)
                    .orElse(new EncryptedPrivateKey());

            existing.setUser(user);
            existing.setEncryptedPrivateKeyJwk(request.getEncryptedPrivateKey());
            existing.setSalt(request.getSalt());
            existing.setIv(request.getIv());

            encryptedPrivateKeyRepo.save(existing);
            return ResponseEntity.ok("Encrypted private key uploaded");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Failed to upload encrypted private key", "message", e.getMessage()));
        }
    }

    @GetMapping("/get-private/{username}")
    public ResponseEntity<?> getEncryptedPrivateKey(@PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            EncryptedPrivateKey key = encryptedPrivateKeyRepo.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Key not found"));

            Map<String, String> result = new HashMap<>();
            result.put("encryptedPrivateKey", key.getEncryptedPrivateKeyJwk());
            result.put("salt", key.getSalt());
            result.put("iv", key.getIv());

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("error", "Key not found", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("error", "Unexpected error", "message", e.getMessage()));
        }
    }
}
