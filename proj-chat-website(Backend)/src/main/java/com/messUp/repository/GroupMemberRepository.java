package com.messUp.repository;

import com.messUp.entity.Group;
import com.messUp.entity.GroupMember;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // Method to find a group member by group ID and username
    Optional<GroupMember> findByGroupIdAndUsername(Long groupId, String username);

    // Method to delete a group member by group ID and username
    void deleteByGroupIdAndUsername(Long groupId, String username);

    boolean existsByGroupAndUser(Group group, User sender);
}
