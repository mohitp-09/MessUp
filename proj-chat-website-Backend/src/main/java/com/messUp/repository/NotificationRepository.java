package com.messUp.repository;

import com.messUp.entity.Notification;
import com.messUp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientAndIsReadFalse(User user);

    List<Notification> findByRecipient(User user);
}
