package com.messUp.repository;

import com.messUp.entity.Friendship;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    boolean existsByUser1AndUser2(User user1, User user2);
    boolean existsByUser2AndUser1(User user1, User user2);
    List<Friendship> findByUser1OrUser2(User user1,User user2);

    @Query("SELECT CASE WHEN f.user1 = :user THEN f.user2 ELSE f.user1 END " +
            "FROM Friendship f WHERE f.user1 = :user OR f.user2 = :user")
    List<User> findFriendsOfUser(User currentUser);
}
