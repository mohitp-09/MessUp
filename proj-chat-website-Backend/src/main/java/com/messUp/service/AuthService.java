package com.messUp.service;

import com.messUp.DTO.AuthResponse;
import com.messUp.DTO.LoginRequest;
import com.messUp.DTO.RegisterRequest;
import com.messUp.JwtUtils.JwtService;
import com.messUp.entity.User;
import com.messUp.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String Token = jwtService.generateToken(user);
        return new AuthResponse(Token, user.getUsername());
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String Token = jwtService.generateToken(user);

        return new AuthResponse(Token, user.getUsername());
    }
}
