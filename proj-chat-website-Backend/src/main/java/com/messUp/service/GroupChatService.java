package com.messUp.service;

import com.messUp.DTO.CreateGroupDTO;
import com.messUp.DTO.GroupDTO;
import com.messUp.DTO.GroupMemberDTO;
import com.messUp.DTO.GroupMessageDTO;
import com.messUp.entity.Group;
import com.messUp.entity.GroupMember;
import com.messUp.entity.GroupMessage;
import com.messUp.entity.User;
import com.messUp.repository.GroupMemberRepository;
import com.messUp.repository.GroupMessageRepository;
import com.messUp.repository.GroupRepository;
import com.messUp.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupChatService {

    private final GroupRepository groupRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupChatService(GroupRepository groupRepository,
                            GroupMessageRepository groupMessageRepository
                            , UserRepository userRepository
                            , GroupMemberRepository groupMemberRepository
                            , SimpMessagingTemplate messagingTemplate) {
        this.groupRepository = groupRepository;
        this.groupMessageRepository = groupMessageRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<GroupMessageDTO> getMessagesForGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMessage> messages = groupMessageRepository.findByGroupOrderByTimestampAsc(group);

        return messages.stream().map(this::convertDTO).collect(Collectors.toList());
    }

    private GroupMessageDTO convertDTO(GroupMessage groupMessage) {
        GroupMessageDTO dto = new GroupMessageDTO();
        dto.setId(groupMessage.getId());
        dto.setGroupId(groupMessage.getGroup().getId());
        if (groupMessage.getSender() != null) {
            dto.setSender(groupMessage.getSender().getUsername());
        } else {
            dto.setSender("System");
        }
        dto.setMessage(groupMessage.getMessage());
        dto.setTimestamp(groupMessage.getTimestamp());
        return dto;
    }

    public void handleGroupMessage(GroupMessageDTO groupMessageDTO) {
        Group group = groupRepository.findById(groupMessageDTO.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User sender = userRepository.findByUsername(groupMessageDTO.getSender())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember =  groupMemberRepository.existsByGroupAndUser(group, sender);
        if (!isMember) {
            throw new RuntimeException("User is not a member of the group");
        }

        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroup(group);
        groupMessage.setSender(sender);
        groupMessage.setMessage(groupMessageDTO.getMessage());
        groupMessage.setTimestamp(java.time.LocalDateTime.now());

        groupMessageRepository.save(groupMessage);

        messagingTemplate.convertAndSend("/topic/group/" + group.getId(), groupMessageDTO);
    }

    public Long createGroup(CreateGroupDTO dto) {
        if(dto.getGroupName() == null || dto.getGroupName().isEmpty()) {
            throw new RuntimeException("Group name cannot be empty");
        }

        Group group = new Group();
        group.setName(dto.getGroupName());
        User creator = userRepository.findByUsername(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Creator not found: " + dto.getCreatedBy()));
        group.setCreatedBy(creator);
        groupRepository.save(group);

        for(String username : dto.getMemberUsernames()){
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            GroupMember gm= new GroupMember();
            gm.setUser(user);
            gm.setGroup(group);
            gm.setAdmin(dto.getCreatedBy().equals(username));
            groupMemberRepository.save(gm);
        }

        GroupMessage systemMessage = new GroupMessage();
        systemMessage.setGroup(group);
        systemMessage.setSender(null);
        systemMessage.setMessage("Group created by " + dto.getCreatedBy());
        systemMessage.setTimestamp(java.time.LocalDateTime.now());
        groupMessageRepository.save(systemMessage);

        GroupMessageDTO sysMsgDTO = new GroupMessageDTO();
        sysMsgDTO.setGroupId(group.getId());
        sysMsgDTO.setSender("Sender");
        sysMsgDTO.setMessage("Group created by " + dto.getCreatedBy());


        messagingTemplate.convertAndSend("/topic/group/" + group.getId(), sysMsgDTO);

        return group.getId();
    }

    public List<Group> getGroupsOfUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        List<GroupMember> memberships = groupMemberRepository.findByUser(user);
        return memberships.stream()
                .map(GroupMember::getGroup)
                .toList();
    }

    public List<GroupMemberDTO> getMembersOfGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMember> members = groupMemberRepository.findByGroup(group);
        return members.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private GroupMemberDTO convertToDTO(GroupMember groupMember) {
        GroupMemberDTO dto = new GroupMemberDTO();
        dto.setUsername(groupMember.getUser().getUsername());
        dto.setAdmin(groupMember.isAdmin());
        dto.setJoinedAt(groupMember.getJoinedAt());
        return dto;
    }

    public void addMemberToGroup(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User is already a member of the group");
        }
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setJoinedAt(java.time.LocalDateTime.now());
        groupMemberRepository.save(groupMember);
    }
}
