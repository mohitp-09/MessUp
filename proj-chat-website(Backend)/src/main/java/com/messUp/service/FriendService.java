package com.messUp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messUp.DTO.FriendStatusDTO;
import com.messUp.entity.FriendRequest;
import com.messUp.entity.Friendship;
import com.messUp.entity.Notification;
import com.messUp.entity.User;
import com.messUp.repository.FriendRequestRepository;
import com.messUp.repository.FriendshipRepository;
import com.messUp.repository.NotificationRepository;
import com.messUp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public FriendStatusDTO sendFriendRequest(String senderUsername, String receiverUsername) throws JsonProcessingException {

        FriendStatusDTO response = new FriendStatusDTO();

        if(senderUsername.equals(receiverUsername)) {
            response.setCanSendRequest(false);
            return response;
        }

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));


        if (friendshipRepository.existsByUser1AndUser2(sender, receiver) ||
            friendshipRepository.existsByUser2AndUser1(sender, receiver)) {
            response.setCanSendRequest(false);
            response.setFriend(true);
            return response;
        }

        if(friendRequestRepository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver, receiver, sender).isPresent()) {
            response.setCanSendRequest(false);
            response.setRequestPending(true);
            return response;
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

        response.setCanSendRequest(false);
        return response;
    }

    public String acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(FriendRequest.Status.ACCEPTED);
        friendRequestRepository.save(request);

        Friendship friendship = new Friendship();
        friendship.setUser1(request.getSender());
        friendship.setUser2(request.getReceiver());
        friendship.setCreatedAt(LocalDateTime.now());
        friendshipRepository.save(friendship);

        return "Friend request accepted successfully.";
    }

    public String rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(FriendRequest.Status.REJECTED);
        friendRequestRepository.save(request);

        friendRequestRepository.delete(request);

        return "Friend request rejected successfully.";
    }

    public List<User> getAllFriends(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);

        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getUser1().getId().equals(id)) {
                friends.add(friendship.getUser2());
            } else {
                friends.add(friendship.getUser1());
            }
        }

        return friends;
    }
}
