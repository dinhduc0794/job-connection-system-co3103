package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.FollowCompanyDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.repository.FollowCompanyRepository;
import com.javaweb.jobconnectionsystem.repository.NotificationRepository;
import com.javaweb.jobconnectionsystem.service.FollowCompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowCompanyServiceImpl implements FollowCompanyService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FollowCompanyRepository followCompanyRepository;

    @Override
    public FollowCompanyEntity saveFollowCompany (FollowCompanyDTO followCompanyDTO) {
        FollowCompanyEntity followCompanyEntity =  modelMapper.map(followCompanyDTO, FollowCompanyEntity.class);
        ApplicantEntity applicantEntity = applicantRepository.findById(followCompanyDTO.getApplicantId()).get();
        CompanyEntity companyEntity = companyRepository.findById(followCompanyDTO.getCompanyId()).get();

        if(followCompanyRepository.existsByApplicantAndCompany(applicantEntity, companyEntity)) {
            throw new RuntimeException("You have followed this company");
        }
        if(followCompanyDTO.getApplicantId() != null) {
            followCompanyEntity.setApplicant(applicantEntity);
        }
        if(followCompanyDTO.getCompanyId() != null) {
            followCompanyEntity.setCompany(companyEntity);
        }
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(companyEntity);
        notificationEntity.setContent(applicantEntity.getFirstName() + " " + applicantEntity.getLastName() + " has been following your company");
        notificationRepository.save(notificationEntity);
        return followCompanyRepository.save(followCompanyEntity);
    }

    @Override
    public FollowCompanyEntity deleteFollowCompanyById (Long id) {
        FollowCompanyEntity followCompanyEntity = followCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        return followCompanyEntity;
    }
}
