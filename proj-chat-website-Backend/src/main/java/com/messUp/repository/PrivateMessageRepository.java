package com.messUp.repository;

import com.messUp.entity.PrivateMessage;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    @Query("SELECT m FROM PrivateMessage m WHERE " +
            "(m.sender.username = :user1 AND m.receiver.username = :user2) OR " +
            "(m.sender.username = :user2 AND m.receiver.username = :user1) " +
            "ORDER BY m.timestamp ASC")
    List<PrivateMessage> findConversationBetween(@Param("user1") String user1, @Param("user2") String user2);

    Optional<PrivateMessage> findTopBySenderAndReceiverOrderByTimestampDesc(User currentUser, User friend);
}
