package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.CompanyConverter;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
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
    public CompanyDetailResponse getCompanyById(Long id) {
        CompanyEntity companyEntity = companyRepository.findById(id).get();
        CompanyDetailResponse companyDetailResponse = companyConverter.toCompanyDetailResponse(companyEntity);
        return companyDetailResponse;
    }

    @Override
    public CompanyEntity saveCompany(CompanyDTO companyDTO) {
        CompanyEntity companyEntity = companyConverter.toCompanyEntity(companyDTO);

        CompanyEntity companyFromTaxCode = companyRepository.findByTaxCode(companyDTO.getTaxCode());
        if (companyFromTaxCode != null) {
            if(companyDTO.getId() == null) throw new RuntimeException("Company tax code already exists");
        }
        CompanyEntity companyFromName = companyRepository.findByName(companyDTO.getName());
        if (companyFromName != null) {
            if(companyDTO.getId() == null) throw new RuntimeException("Company name already exists");
        }

        companyRepository.save(companyEntity);

        List<PhoneNumberEntity> oldPhoneNumberEntitys = phoneNumberRepository.findByUser_Id(companyEntity.getId());
        if (oldPhoneNumberEntitys != null && !oldPhoneNumberEntitys.isEmpty()) {
            phoneNumberRepository.deleteAll(oldPhoneNumberEntitys);
        }

        for (String phoneNumber : companyDTO.getPhoneNumbers()) {
            if (phoneNumberRepository.existsByPhoneNumber(phoneNumber)) {
                throw new RuntimeException("Phone number already exists: " + phoneNumber);
            }

            PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
            phoneNumberEntity.setPhoneNumber(phoneNumber);
            phoneNumberEntity.setUser(companyEntity);
            phoneNumberRepository.save(phoneNumberEntity);
        }

        List<EmailEntity> oldEmailrEntitys = emailRepository.findByUser_Id(companyEntity.getId());
        if (oldEmailrEntitys != null && !oldEmailrEntitys.isEmpty()) {
            emailRepository.deleteAll(oldEmailrEntitys);
        }

        for (String email : companyDTO.getEmails()) {
            if (emailRepository.existsByEmail(email)) {
                throw new RuntimeException("email already exists: " + email);
            }

            EmailEntity emailEntity = new EmailEntity();
            emailEntity.setEmail(email);
            emailEntity.setUser(companyEntity);
            emailRepository.save(emailEntity);
        }

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
