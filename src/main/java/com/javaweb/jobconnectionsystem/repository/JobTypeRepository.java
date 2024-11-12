package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTypeRepository extends JpaRepository<JobTypeEntity,Long> {
}
