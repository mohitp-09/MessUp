package com.messUp.controller;

import com.messUp.DTO.PrivateMessageDTO;
import com.messUp.DTO.ReadReceiptDTO;
import com.messUp.entity.PrivateMessage;
import com.messUp.entity.User;
import com.messUp.repository.PrivateMessageRepository;
import com.messUp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

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
        privateMessage.setMediaUrl(privateMessageDTO.getMediaUrl());
        privateMessage.setMediaType(privateMessageDTO.getMediaType());
        privateMessage.setStatus(PrivateMessage.MessageStatus.SENT);
        privateMessage.setTimestamp(java.time.LocalDateTime.now());

        privateMessageRepository.save(privateMessage);

        privateMessageDTO.setTimestamp(privateMessage.getTimestamp());
        privateMessageDTO.setMessageId(privateMessage.getId());

        messagingTemplate.convertAndSendToUser(privateMessageDTO.getReceiver(), "/private", privateMessageDTO);

        privateMessage.setStatus(PrivateMessage.MessageStatus.DELIVERED);
        privateMessageRepository.save(privateMessage);
    }

    @MessageMapping("/markAsRead")
    public void markAsRead(ReadReceiptDTO readReceiptDTO) {
        privateMessageRepository.findById(readReceiptDTO.getMessageId())
                .ifPresent(privateMessage -> {
                    privateMessage.setStatus(PrivateMessage.MessageStatus.READ);
                    privateMessageRepository.save(privateMessage);
                    messagingTemplate.convertAndSendToUser(privateMessage.getSender().getUsername(), "/private/read-receipts", readReceiptDTO);
                });
    }

    @GetMapping("oldChat/{username}")
    public ResponseEntity<List<PrivateMessageDTO>> getChatHistory(Principal principal,@PathVariable String username) {
        String currentUsername = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        List<PrivateMessage> messages = privateMessageRepository.findConversationBetween(currentUsername, username);

        List<PrivateMessageDTO> privateMessageDTOs = messages.stream()
                .map(message -> {
                    PrivateMessageDTO dto = new PrivateMessageDTO();
                    dto.setMessage(message.getMessage());
                    dto.setSender(message.getSender().getUsername());
                    dto.setReceiver(message.getReceiver().getUsername());
                    dto.setMediaUrl(message.getMediaUrl());
                    dto.setMediaType(message.getMediaType());
                    dto.setTimestamp(message.getTimestamp());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(privateMessageDTOs);
    }
}
