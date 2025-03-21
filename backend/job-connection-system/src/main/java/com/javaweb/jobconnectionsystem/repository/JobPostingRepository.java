package com.javaweb.jobconnectionsystem.repository;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.repository.custom.JobPostingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPostingEntity, Long>, JobPostingRepositoryCustom {
    List<JobPostingEntity> findAllByStatus(Boolean status);
}
