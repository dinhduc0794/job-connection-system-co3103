package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    // Thêm thông báo
    NotificationEntity addNotification(NotificationEntity notification);

    // Lấy tất cả thông báo
    List<NotificationEntity> getAllNotifications();

    // Lấy thông báo theo ID
    Optional<NotificationEntity> getNotificationById(Long id);

    // Cập nhật thông báo
    NotificationEntity updateNotification(Long id, NotificationEntity notificationDetails);

    // Xóa thông báo theo ID
    void deleteNotificationById(Long id);
}
