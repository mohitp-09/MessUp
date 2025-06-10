package com.messUp.controller;

import com.messUp.DTO.FriendStatusDTO;
import com.messUp.repository.UserRepository;
import com.messUp.service.FriendService;
import com.messUp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    public FriendController(FriendService friendService, UserService userService, UserRepository userRepository) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam String senderUsername, @RequestParam String receiverUsername) {
        try {
            FriendStatusDTO response = friendService.sendFriendRequest(senderUsername, receiverUsername);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending friend request: " + e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam Long requestId) {
        try {
            String response = friendService.acceptFriendRequest(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error accepting friend request: " + e.getMessage());
        }
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestParam Long requestId) {
        try {
            String response = friendService.rejectFriendRequest(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error rejecting friend request: " + e.getMessage());
        }
    }

    @GetMapping("/getAllFriends")
    public ResponseEntity<?> getAllFriends(Principal principal) {
        try {
            String username = principal.getName();
            Long id = userService.getIdByUsername(username);
            return ResponseEntity.ok(friendService.getAllFriends(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving friends: " + e.getMessage());
        }
    }
}

