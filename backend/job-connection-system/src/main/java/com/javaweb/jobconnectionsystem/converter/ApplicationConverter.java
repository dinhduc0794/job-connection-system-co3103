package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.JobPostingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;
    public ApplicationEntity toApplicationEntity(ApplicationDTO applicationDTO){
        ApplicationEntity applicationEntity = modelMapper.map(applicationDTO, ApplicationEntity.class);
        if(applicationDTO.getApplicantId()!=null) {
            ApplicantEntity applicantEntity = applicantRepository.findById(applicationDTO.getApplicantId()).get();
            applicationEntity.setApplicant(applicantEntity);
            applicantEntity.getApplications().add(applicationEntity);
        }
        if(applicationDTO.getJobPostingId()!=null) {
            JobPostingEntity jobPostingEntity = jobPostingRepository.findById(applicationDTO.getJobPostingId()).get();
            applicationEntity.setJobPosting(jobPostingEntity);
            jobPostingEntity.getApplications().add(applicationEntity);
        }
        return applicationEntity;
    }
}
