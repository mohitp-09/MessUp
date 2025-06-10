package com.messUp.repository;

import com.messUp.entity.FriendRequest;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<Object> findBySenderAndReceiverOrReceiverAndSender(User sender1, User receiver1, User receiver2,  User sender2);
}
