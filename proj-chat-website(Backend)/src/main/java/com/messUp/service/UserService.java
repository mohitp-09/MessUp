package com.messUp.service;

import com.messUp.DTO.UserDTO;
import com.messUp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUser(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getProfilePicture()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
