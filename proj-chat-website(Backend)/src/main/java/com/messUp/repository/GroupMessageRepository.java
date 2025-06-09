package com.messUp.repository;

import com.messUp.entity.Group;
import com.messUp.entity.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {

   List<GroupMessage> findByGroupOrderByTimestampAsc(Group group);
}
