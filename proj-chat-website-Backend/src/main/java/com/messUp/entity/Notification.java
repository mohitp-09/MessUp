package com.messUp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User recipient;

    private Long referenceId;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Type {
        FRIEND_REQUEST,
        MESSAGE,
        GROUP_INVITATION,
        OTHER
    }
}
