package com.messUp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @Column(nullable = false)
    private boolean isAdmin = false;

    private LocalDateTime joinedAt = LocalDateTime.now();
}
