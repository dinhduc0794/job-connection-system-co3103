package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;


import java.util.List;
import java.util.Optional;

public interface CompanyService {
    public CompanyEntity addCompany(CompanyEntity company);

    public List<CompanyEntity> getAllCompanies();
    // Lấy user theo ID
    public Optional<CompanyEntity> getCompanyById(Long id);
    // Sửa use
    public CompanyEntity updateCompany(Long id, CompanyEntity companyDetails);
    // Xoa user
    public void deleteCompanyById(Long id);
}
