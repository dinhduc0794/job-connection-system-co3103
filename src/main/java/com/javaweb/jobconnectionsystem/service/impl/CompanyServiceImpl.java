package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public CompanyEntity addCompany(CompanyEntity company) {
        CompanyEntity companyFromDb = companyRepository.findByTaxCode(company.getTaxCode());
        if (companyFromDb != null) {
            if (company.getTaxCode().equals(companyFromDb.getTaxCode())) {
                throw new RuntimeException("Company taxcode already exists");
            }}
        return companyRepository.save(company);
    }

    @Override
    public Optional<CompanyEntity> getCompanyById(Long id) {
        Optional<CompanyEntity> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return null;
        }
        return company;
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
