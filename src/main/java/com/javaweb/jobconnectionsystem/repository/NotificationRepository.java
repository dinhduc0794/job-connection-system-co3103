package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
}
