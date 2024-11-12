package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Integer> {
}
