package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.response.CompanyPublicResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;


import java.util.List;
import java.util.Optional;

public interface CompanyService {
    ResponseDTO saveCompany(CompanyDTO companyDTO);

    List<CompanyPublicResponse> getAllCompanies(CompanySearchRequest params);

    int countTotalItems(CompanySearchRequest params);
    // Lấy user theo ID
    CompanyPublicResponse getCompanyDetailResponseById(Long id);

    CompanyDTO getCompanyById(Long id);

    // Sửa use
    CompanyEntity updateCompany(Long id, CompanyEntity companyDetails);
    // Xoa user
    void deleteCompanyById(Long id);
    void updateCompanyRating(CompanyEntity company);
    double calculateAverageRating(CompanyEntity company);
}
