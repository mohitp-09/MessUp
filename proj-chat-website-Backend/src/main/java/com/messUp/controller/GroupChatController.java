package com.messUp.controller;

import com.messUp.DTO.CreateGroupDTO;
import com.messUp.DTO.GroupMessageDTO;
import com.messUp.entity.Group;
import com.messUp.entity.GroupMember;
import com.messUp.entity.GroupMessage;
import com.messUp.entity.User;
import com.messUp.repository.GroupMemberRepository;
import com.messUp.repository.GroupMessageRepository;
import com.messUp.repository.GroupRepository;
import com.messUp.repository.UserRepository;
import com.messUp.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller()
@RequestMapping("/groupChat")
public class GroupChatController {
    private final GroupChatService groupChatService;

    public GroupChatController(GroupChatService groupChatService) {

        this.groupChatService = groupChatService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupDTO dto) {
        Long gId=groupChatService.createGroup(dto);

        return ResponseEntity.ok("Group created with ID: " + gId);
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMemberToGroup(@RequestParam Long groupId, @RequestParam String username) {
        groupChatService.addMemberToGroup(groupId, username);
        return ResponseEntity.ok("Member added to group successfully.");
    }

    @MessageMapping("/groupMessage")
    public void handleGroupMessage(GroupMessageDTO groupMessageDTO) {

        groupChatService.handleGroupMessage(groupMessageDTO);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupMessages(@PathVariable Long groupId) {

        return ResponseEntity.ok(groupChatService.getMessagesForGroup(groupId));
    }


    @GetMapping("/getGroups")
    public ResponseEntity<?> getGroups(Principal principal) {
        return ResponseEntity.ok(groupChatService.getGroupsOfUser(principal.getName()));
    }

    @GetMapping("/getGroupMembers/{groupId}")
    public ResponseEntity<?> getGroupMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupChatService.getMembersOfGroup(groupId));
    }
}
