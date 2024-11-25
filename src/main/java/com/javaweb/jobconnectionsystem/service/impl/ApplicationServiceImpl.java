package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.ApplicationConverter;
import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.NotificationEntity;
import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.ApplicationRepository;
import com.javaweb.jobconnectionsystem.repository.JobPostingRepository;
import com.javaweb.jobconnectionsystem.repository.NotificationRepository;
import com.javaweb.jobconnectionsystem.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private ApplicationConverter applicationConverter;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public ApplicationEntity saveApplication(ApplicationDTO applicationDTO){
        ApplicationEntity applicationEntity = applicationConverter.toApplicationEntity(applicationDTO);
        if(applicationDTO.getId() == null) { // tao moi application
            NotificationEntity notificationEntity = new NotificationEntity();
            if (applicationEntity.getJobPosting().getCompany() != null) {
                notificationEntity.setUser(applicationEntity.getJobPosting().getCompany());
            } else {
                throw new RuntimeException("Company information is missing in job posting");
            }
            notificationEntity.setContent(applicationEntity.getApplicant().getFirstName() + " has registered your job posting " + applicationEntity.getJobPosting().getTitle());
            notificationRepository.save(notificationEntity);
            return applicationRepository.save(applicationEntity);
        }
        else { // chinh sua thong tin application
            if(applicationDTO.getStatus() == StatusEnum.WAITING) { // chinh sua thong tin description
                return applicationRepository.save(applicationEntity);
            }
            else {
                NotificationEntity notificationEntity = new NotificationEntity();
                if (applicationEntity.getApplicant() != null) {
                    notificationEntity.setUser(applicationEntity.getApplicant());
                } else {
                    throw new RuntimeException("Application information is missing");
                }
                notificationEntity.setContent(applicationEntity.getJobPosting().getCompany().getName() + " has " + applicationDTO.getStatus() + " your application for the job " + applicationEntity.getJobPosting().getTitle());
                notificationRepository.save(notificationEntity);
                return applicationRepository.save(applicationEntity);
            }
        }
    }

    @Override
    public List<ApplicationEntity> getAllApplication() {
        return null;
    }

    @Override
    public void DeleteApplication(Long applicationID) {

    }
}
