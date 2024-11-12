package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity,Long> {
}
