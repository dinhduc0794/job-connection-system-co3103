package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity,Long> {
}
