package com.messUp.controller;

import com.messUp.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam String senderUsername, @RequestParam String receiverUsername) {
        try {
            friendService.sendFriendRequest(senderUsername, receiverUsername);
            return ResponseEntity.ok("Friend request sent successfully.");
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
}
