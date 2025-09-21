package com.messUp.controller;

import com.messUp.entity.Notification;
import com.messUp.entity.User;
import com.messUp.repository.UserRepository;
import com.messUp.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationService.getUnReadNotifications(user);
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok("Notification marked as read.");
    }

    @GetMapping("/all")
    public List<Notification> getAllNotifications(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationService.getAllNotifications(user);
    }
}
