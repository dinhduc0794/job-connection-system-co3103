package com.javaweb.jobconnectionsystem.repository;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPostingEntity, Long> {
}
