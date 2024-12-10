package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.CompanyConverter;
import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.response.CompanyPublicResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.repository.EmailRepository;
import com.javaweb.jobconnectionsystem.repository.PhoneNumberRepository;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<CompanyPublicResponse> getAllCompanies(CompanySearchRequest params) {
        List<CompanyEntity> companyEntities = companyRepository.findAll(params);

        List<CompanyPublicResponse> companyResponses = new ArrayList<>();
        for (CompanyEntity ent : companyEntities) {
            companyResponses.add(companyConverter.toCompanyPublicResponse(ent));
        }
        return companyResponses;
    }

    @Override
    public int countTotalItems(CompanySearchRequest params) {
        return companyRepository.countTotalItems(params);
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        CompanyEntity companyEntity = companyRepository.findById(id).get();
        CompanyDTO companyDTO = companyConverter.toCompanyDTO(companyEntity);
        return companyDTO;
    }

    @Override
    public CompanyPublicResponse getCompanyDetailResponseById(Long id) {
        CompanyEntity companyEntity = companyRepository.findById(id).get();
        CompanyPublicResponse companyPublicResponse = companyConverter.toCompanyPublicResponse(companyEntity);
        return companyPublicResponse;
    }

    @Override
    public ResponseDTO saveCompany(CompanyDTO companyDTO) {
//        String encodedPassword = passwordEncoder.encode(companyDTO.getPassword());
//        companyDTO.setPassword(encodedPassword);

        ResponseDTO responseDTO = new ResponseDTO();
        if (companyDTO.getId() != null) {
            if (!companyRepository.existsById(companyDTO.getId())) {
                responseDTO.setMessage("Không tìm thấy công ty cần sửa");
                return responseDTO;
            }
            responseDTO.setMessage("Sửa thông tin công ty thành công");
        }
        else {
            responseDTO.setMessage("Đăng ký công ty mới thành công");
        }
        CompanyEntity companyEntity = companyConverter.toCompanyEntity(companyDTO);
        responseDTO.setData(companyEntity);
        companyEntity = companyRepository.save(companyEntity);
        return responseDTO;
    }

    @Override
    public CompanyEntity updateCompany(Long id, CompanyEntity companyDetails) {
        CompanyEntity company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        if (companyRepository.findByTaxCode(companyDetails.getTaxCode())!= null ) {
            throw new RuntimeException("Mã số thuế đã tồn tại");
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
