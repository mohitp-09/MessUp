package com.messUp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=true)
    private String password;

    @Column(nullable=false)
    private String email;

    private String profilePicture;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    private Boolean onlineStatus = false;
}
