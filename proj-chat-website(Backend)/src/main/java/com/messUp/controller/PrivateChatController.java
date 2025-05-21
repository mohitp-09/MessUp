package com.messUp.controller;

import com.messUp.DTO.PrivateMessageDTO;
import com.messUp.entity.PrivateMessage;
import com.messUp.entity.User;
import com.messUp.repository.PrivateMessageRepository;
import com.messUp.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PrivateChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PrivateMessageRepository privateMessageRepository;
    private final UserRepository userRepository;

    public PrivateChatController(SimpMessagingTemplate messagingTemplate,
                                 PrivateMessageRepository privateMessageRepository,
                                 UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.privateMessageRepository = privateMessageRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/sendPrivateMessage")
    public void sendPrivateMessage(PrivateMessageDTO privateMessageDTO) {
        User sender = userRepository.findByUsername(privateMessageDTO.getSender()).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(privateMessageDTO.getReceiver()).orElseThrow(() -> new RuntimeException("Receiver not found"));

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(sender);
        privateMessage.setReceiver(receiver);
        privateMessage.setMessage(privateMessageDTO.getMessage());
        privateMessage.setTimestamp(java.time.LocalDateTime.now());

        privateMessageRepository.save(privateMessage);

        messagingTemplate.convertAndSendToUser(privateMessageDTO.getReceiver(), "/private", privateMessageDTO);
    }
}
