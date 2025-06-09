package com.messUp.service;

import com.messUp.DTO.RecentChatDTO;
import com.messUp.entity.PrivateMessage;
import com.messUp.entity.User;
import com.messUp.repository.PrivateMessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final UserService userService;
    private final FriendService friendService;
    private final PrivateMessageRepository privateMessageRepository;

    public ChatService(UserService userService, FriendService friendService, PrivateMessageRepository privateMessageRepository) {
        this.userService = userService;
        this.friendService = friendService;
        this.privateMessageRepository = privateMessageRepository;
    }

    public List<RecentChatDTO> getRecentChats(String username) {
        User currentUser = userService.getUserByUsername(username);

        List<User> friends = friendService.getFriends(currentUser);

        List<RecentChatDTO> recentChats = new ArrayList<>();

        for(User friend : friends) {
            Optional<PrivateMessage> lastMsg = privateMessageRepository.
                    findTopBySenderAndReceiverOrderByTimestampDesc(currentUser, friend);

            if(lastMsg.isEmpty()){
                lastMsg = privateMessageRepository.findTopBySenderAndReceiverOrderByTimestampDesc(friend, currentUser);
            }

            recentChats.add(new RecentChatDTO(
                    friend.getUsername(),
                    friend.getProfilePicture(),
                    lastMsg.orElse(null),
                    currentUser.getUsername()
            ));
        }

        return recentChats;
    }

}
