package com.messUp.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String profilePicture;

    public UserDTO(Long id, String username, String profilePicture) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }
}
