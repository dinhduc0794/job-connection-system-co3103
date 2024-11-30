package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.CompanyDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.CompanySearchResponse;


import java.util.List;
import java.util.Optional;

public interface CompanyService {
    CompanyEntity saveCompany(CompanyDTO companyDTO);

    List<CompanySearchResponse> getAllCompanies(CompanySearchRequest params);

    int countTotalItems(CompanySearchRequest params);
    // Lấy user theo ID
    CompanyDetailResponse getCompanyDetailResponseById(Long id);

    Optional<CompanyEntity> getCompanyEntityById(Long id);

    // Sửa use
    CompanyEntity updateCompany(Long id, CompanyEntity companyDetails);
    // Xoa user
    void deleteCompanyById(Long id);
}
