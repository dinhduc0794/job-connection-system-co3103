package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateCompanyRepository extends JpaRepository<RateCompanyEntity, Long> {
    boolean existsByApplicantAndCompany (ApplicantEntity applicantEntity, CompanyEntity companyEntity);
}
