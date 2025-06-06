package com.messUp.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMessageDTO {
    private Long groupId;
    private String sender;
    private String message;
    private LocalDateTime timestamp;
}
