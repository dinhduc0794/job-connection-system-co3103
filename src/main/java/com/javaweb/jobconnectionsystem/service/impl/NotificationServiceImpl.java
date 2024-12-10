package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.NotificationEntity;
import com.javaweb.jobconnectionsystem.repository.NotificationRepository;
import com.javaweb.jobconnectionsystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public NotificationEntity addNotification(NotificationEntity notification) {
        if (notification == null) {
            return null;
        }
        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationEntity> getAllNotifications(Long userId) {
        List<NotificationEntity> notifications = notificationRepository.findByUserId(userId);
        if(notifications == null) {
            throw new RuntimeException("No notifications found for user " + userId);
        }
        return notifications;
    }

    @Override
    public Optional<NotificationEntity> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public NotificationEntity updateNotification(Long id, NotificationEntity notificationDetails) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setContent(notificationDetails.getContent());
        notification.setUser(notificationDetails.getUser());

        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotificationById(Long id) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }
}
