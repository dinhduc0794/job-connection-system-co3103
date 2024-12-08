package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.repository.custom.CompanyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long>, CompanyRepositoryCustom {
    @Query("SELECT c FROM CompanyEntity c WHERE c.taxCode = :taxCode")
    CompanyEntity findByTaxCode(@Param("taxCode") String taxCode);
    CompanyEntity findByName(String companyName);
    @Query("SELECT AVG(rc.rate) FROM RateCompanyEntity rc WHERE rc.company.id = :companyId")
    Double calculateAverageRating(@Param("companyId") Long companyId);
    CompanyEntity findByUsername(String username);
}
