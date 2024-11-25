package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.repository.NotificationRepository;
import com.javaweb.jobconnectionsystem.repository.RateCompanyRepository;
import com.javaweb.jobconnectionsystem.service.RateCompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public RateCompanyEntity saveRate(RateCompanyDTO rateCompanyDTO) {
        RateCompanyEntity rateCompanyEntity = modelMapper.map(rateCompanyDTO, RateCompanyEntity.class);

        ApplicantEntity applicantEntity = applicantRepository.findById(rateCompanyDTO.getApplicantId()).get();
        CompanyEntity companyEntity = companyRepository.findById(rateCompanyDTO.getCompanyId()).get();

        if(rateCompanyRepository.existsByApplicantAndCompany(applicantEntity, companyEntity)) {
            throw new RuntimeException("You have rated this company");
        }
        if(rateCompanyDTO.getApplicantId() != null) {
            rateCompanyEntity.setApplicant(applicantEntity);
        }
        if(rateCompanyDTO.getCompanyId() != null) {
            rateCompanyEntity.setCompany(companyEntity);
        }

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(companyEntity);
        notificationEntity.setContent(applicantEntity.getFirstName() + " " + applicantEntity.getLastName() + " has been rating your company " + rateCompanyDTO.getRate());
        notificationRepository.save(notificationEntity);

        return rateCompanyRepository.save(rateCompanyEntity);
    }
}
