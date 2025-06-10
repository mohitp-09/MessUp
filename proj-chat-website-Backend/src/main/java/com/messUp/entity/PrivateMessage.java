package com.messUp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "private_messages")
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    public enum MediaType {
       TEXT, IMAGE, VIDEO, AUDIO,NONE
    }

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT;

    public enum MessageStatus {
        SENT, DELIVERED, READ
    }

    private LocalDateTime timestamp = LocalDateTime.now();

}
