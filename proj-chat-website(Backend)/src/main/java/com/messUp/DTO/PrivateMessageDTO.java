package com.messUp.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrivateMessageDTO {

    private String message;
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;

}
