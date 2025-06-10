package com.messUp.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMemberDTO {
    private String username;
    private String profilePicture;
    private boolean isAdmin;
    private LocalDateTime joinedAt;

    public GroupMemberDTO() {
    }

    public GroupMemberDTO(String username, String profilePicture, boolean isAdmin, LocalDateTime joinedAt) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.isAdmin = isAdmin;
        this.joinedAt = joinedAt;
    }
}
