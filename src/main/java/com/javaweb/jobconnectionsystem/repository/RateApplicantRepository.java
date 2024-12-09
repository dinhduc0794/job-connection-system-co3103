package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.RateApplicantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateApplicantRepository extends JpaRepository<RateApplicantEntity, Long> {

}
