package com.messUp.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String profilePicture;

    public UserDTO(Long id, String username, String profilePicture, String email) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.email = email;
    }
}
