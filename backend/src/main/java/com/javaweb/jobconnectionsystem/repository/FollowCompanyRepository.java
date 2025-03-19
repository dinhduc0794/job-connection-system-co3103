package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.FollowCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowCompanyRepository extends JpaRepository<FollowCompanyEntity, Long> {
    boolean existsByApplicantAndCompany (ApplicantEntity applicantEntity, CompanyEntity companyEntity);
}
