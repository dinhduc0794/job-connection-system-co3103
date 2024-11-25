package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.InterestedPostEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestedPostRepository extends JpaRepository<InterestedPostEntity, Long> {
    boolean existsByApplicantAndJobPosting(ApplicantEntity applicantEntity, JobPostingEntity jobPostingEntity);
    InterestedPostEntity findByApplicantAndJobPosting(ApplicantEntity applicantEntity, JobPostingEntity jobPostingEntity);
}
