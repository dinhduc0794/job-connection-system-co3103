package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
}
