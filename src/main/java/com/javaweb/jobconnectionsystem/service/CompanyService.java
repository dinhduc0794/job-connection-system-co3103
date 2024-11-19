package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.response.CompanySearchResponse;


import java.util.List;
import java.util.Optional;

public interface CompanyService {
    public CompanyEntity saveCompany(CompanyDTO companyDTO);

    public List<CompanySearchResponse> getAllCompanies(CompanySearchRequest params);
    // Lấy user theo ID
    public Optional<CompanyEntity> getCompanyById(Long id);
    // Sửa use
    public CompanyEntity updateCompany(Long id, CompanyEntity companyDetails);
    // Xoa user
    public void deleteCompanyById(Long id);
}
