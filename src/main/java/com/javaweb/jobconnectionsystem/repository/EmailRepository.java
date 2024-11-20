package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    public boolean existsByEmail(String email);
    public void deleteByEmail(String email);
}
