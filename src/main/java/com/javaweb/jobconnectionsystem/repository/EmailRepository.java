package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
}
