package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
    @Query("SELECT c FROM CompanyEntity c WHERE c.taxCode = :taxCode")
    CompanyEntity findByTaxCode(@Param("taxCode") String taxCode);
}
