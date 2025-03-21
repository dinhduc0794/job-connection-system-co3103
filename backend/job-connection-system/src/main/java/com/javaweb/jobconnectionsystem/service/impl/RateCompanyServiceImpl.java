package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.repository.NotificationRepository;
import com.javaweb.jobconnectionsystem.repository.RateCompanyRepository;
import com.javaweb.jobconnectionsystem.service.RateCompanyService;
import org.hibernate.sql.ast.tree.expression.Over;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class RateCompanyServiceImpl implements RateCompanyService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RateCompanyRepository rateCompanyRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public RateCompanyDTO getRateCompanyId(Long id) {
        RateCompanyEntity rateCompanyEntity = rateCompanyRepository.findById(id).orElseThrow(() -> new RuntimeException("Rate not found"));
        RateCompanyDTO rateCompanyDTO = modelMapper.map(rateCompanyEntity, RateCompanyDTO.class);
        rateCompanyDTO.setCompanyId(rateCompanyEntity.getCompany().getId());
        rateCompanyDTO.setApplicantId(rateCompanyEntity.getApplicant().getId());
        rateCompanyDTO.setCompanyName(rateCompanyEntity.getCompany().getName());
        return rateCompanyDTO;
    }

    @Override
    public ResponseDTO saveRate(RateCompanyDTO rateCompanyDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        RateCompanyEntity rateCompanyEntity = modelMapper.map(rateCompanyDTO, RateCompanyEntity.class);
        rateCompanyEntity.setApplicant(applicantRepository.findById(rateCompanyDTO.getApplicantId()).get());
        rateCompanyEntity.setCompany(companyRepository.findById(rateCompanyDTO.getCompanyId()).get());

        ApplicantEntity applicantEntity = applicantRepository.findById(rateCompanyDTO.getApplicantId()).get();
        CompanyEntity companyEntity = companyRepository.findById(rateCompanyDTO.getCompanyId()).get();

        if(rateCompanyDTO.getId() != null || rateCompanyRepository.existsByApplicantAndCompany(applicantEntity, companyEntity)) {
            responseDTO.setMessage("Update rate successfully");
        }
        else {
            responseDTO.setMessage("Create rate successfully");
        }

        if(rateCompanyDTO.getApplicantId() != null) {
            rateCompanyEntity.setApplicant(applicantEntity);
        }
        if(rateCompanyDTO.getCompanyId() != null) {
            rateCompanyEntity.setCompany(companyEntity);
        }

        companyRepository.save(companyEntity);

        // Lưu thông báo cho công ty
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(companyEntity);
        notificationEntity.setContent(applicantEntity.getFirstName() + " " + applicantEntity.getLastName() + " has been rating your company " + rateCompanyDTO.getRate() + " stars" + " with feedback : " + rateCompanyDTO.getFeedback());
        notificationRepository.save(notificationEntity);

        rateCompanyRepository.save(rateCompanyEntity);
        responseDTO.setData(rateCompanyEntity);
        return responseDTO;
    }

    @Override
    public List<RateCompanyDTO> getRateCompanyByApplicantId(Long applicantId){
        List<RateCompanyEntity> rateCompanyEntities = rateCompanyRepository.findAllByApplicant_Id(applicantId);
        List<RateCompanyDTO> rateCompanyDT0s = new ArrayList<>();
        for (RateCompanyEntity rateCompanyEntity : rateCompanyEntities) {
            RateCompanyDTO rateCompanyDTO = modelMapper.map(rateCompanyEntity, RateCompanyDTO.class);
            rateCompanyDTO.setCompanyId(rateCompanyEntity.getCompany().getId());
            rateCompanyDTO.setApplicantId(rateCompanyEntity.getApplicant().getId());
            rateCompanyDTO.setCompanyName(rateCompanyEntity.getCompany().getName());
            rateCompanyDT0s.add(rateCompanyDTO);
        }
        return rateCompanyDT0s;
    }

    @Override
    public List<RateCompanyDTO> getRateCompanyByCompanyId(Long companyId){
        List<RateCompanyEntity> rateCompanyEntities = rateCompanyRepository.findAllByCompany_Id(companyId);
        List<RateCompanyDTO> rateCompanyDT0s = new ArrayList<>();
        for (RateCompanyEntity rateCompanyEntity : rateCompanyEntities) {
            RateCompanyDTO rateCompanyDTO = modelMapper.map(rateCompanyEntity, RateCompanyDTO.class);
            rateCompanyDTO.setCompanyId(rateCompanyEntity.getCompany().getId());
            rateCompanyDTO.setApplicantId(rateCompanyEntity.getApplicant().getId());
            rateCompanyDTO.setCompanyName(rateCompanyEntity.getCompany().getName());
            rateCompanyDT0s.add(rateCompanyDTO);
        }
        return rateCompanyDT0s;
    }

    @Override
    public void deleteRate(Long id) {
        RateCompanyEntity rateCompanyEntity = rateCompanyRepository.findById(id).orElseThrow(() -> new RuntimeException("Rate not found"));
        rateCompanyRepository.deleteById(id);
    }
}
