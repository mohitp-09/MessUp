package com.messUp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messUp.entity.FriendRequest;
import com.messUp.entity.Notification;
import com.messUp.entity.User;
import com.messUp.repository.FriendRequestRepository;
import com.messUp.repository.FriendshipRepository;
import com.messUp.repository.NotificationRepository;
import com.messUp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public FriendService(UserRepository userRepository,
                         FriendRequestRepository friendRequestRepository,
                         FriendshipRepository friendshipRepository,NotificationRepository notificationRepository,NotificationService notificationService) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    public String sendFriendRequest(String senderUsername, String receiverUsername) throws JsonProcessingException {
        if(senderUsername.equals(receiverUsername)) {
            return "You cannot send a friend request to yourself.";
        }

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (friendshipRepository.existsByUser1AndUser2(sender, receiver) ||
            friendshipRepository.existsByUser2AndUser1(sender, receiver)) {
            return "You are already friends with this user.";
        }

        if(friendRequestRepository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver, receiver, sender).isPresent()) {
            throw new RuntimeException("Friend request already exists between these users.");
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendRequest.Status.PENDING);
        friendRequestRepository.save(friendRequest);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("senderId", sender.getId());
        metadata.put("receiverId", receiver.getId());
        metadata.put("requestId", friendRequest.getId());

        String metadataJSON=objectMapper.writeValueAsString(metadata);

        notificationService.setNotification(receiver, Notification.Type.FRIEND_REQUEST,
                sender.getUsername() + " has sent you a friend request.",sender.getId(),metadataJSON);

        return "Friend request sent successfully.";
    }

    public String acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(FriendRequest.Status.ACCEPTED);
        friendRequestRepository.save(request);
        return "Friend request accepted successfully.";
    }

    public String rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(FriendRequest.Status.REJECTED);
        friendRequestRepository.save(request);
        return "Friend request rejected successfully.";
    }
}
