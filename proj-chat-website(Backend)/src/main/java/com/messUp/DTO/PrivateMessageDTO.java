package com.messUp.DTO;

import lombok.Data;

@Data
public class PrivateMessageDTO {

    private String message;
    private String sender;
    private String receiver;

}
