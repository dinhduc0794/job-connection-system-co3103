package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.CompanyConverter;
import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.CompanyDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.CompanySearchResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.repository.EmailRepository;
import com.javaweb.jobconnectionsystem.repository.PhoneNumberRepository;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private EmailRepository emailRepository;

    @Override
    public List<CompanySearchResponse> getAllCompanies(CompanySearchRequest params) {
        List<CompanyEntity> companyEntities = companyRepository.findAll(params);

        List<CompanySearchResponse> companyResponses = new ArrayList<>();
        for (CompanyEntity ent : companyEntities) {
            companyResponses.add(companyConverter.toCompanySearchResponse(ent));
        }
        return companyResponses;
    }

    @Override
    public int countTotalItems(CompanySearchRequest params) {
        return companyRepository.countTotalItems(params);
    }

    @Override
    public Optional<CompanyEntity> getCompanyEntityById(Long id) {
        Optional<CompanyEntity> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return null;
        }
        return company;
    }

    @Override
    public CompanyDetailResponse getCompanyDetailResponseById(Long id) {
        CompanyEntity companyEntity = companyRepository.findById(id).get();
        CompanyDetailResponse companyDetailResponse = companyConverter.toCompanyDetailResponse(companyEntity);
        return companyDetailResponse;
    }

    @Override
    public CompanyEntity saveCompany(CompanyDTO companyDTO) {
        CompanyEntity companyEntity = companyConverter.toCompanyEntity(companyDTO);
        companyEntity = companyRepository.save(companyEntity);
        return companyEntity;
    }

    @Override
    public CompanyEntity updateCompany(Long id, CompanyEntity companyDetails) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        if (companyRepository.findByTaxCode(companyDetails.getTaxCode())!= null ) {
            throw new RuntimeException("Company name already exists");
        }
        company.setName(companyDetails.getName());
        company.setTaxCode(companyDetails.getTaxCode());
        return companyRepository.save(company);
    }

    @Override
    public void deleteCompanyById(Long id) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        companyRepository.delete(company);
    }
}
