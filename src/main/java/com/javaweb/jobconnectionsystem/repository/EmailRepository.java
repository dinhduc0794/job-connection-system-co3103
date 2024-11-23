package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    public boolean existsByEmail(String email);
    // public void deleteByEmail(String email);
    public List<EmailEntity> findByUser_Id(Long companyId);
    // public EmailEntity findByEmail(String email);
    @Query("SELECT e FROM EmailEntity e WHERE e.email = :email")
    EmailEntity findByEmail(@Param("email") String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM EmailEntity e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);

}