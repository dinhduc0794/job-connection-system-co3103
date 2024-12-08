package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.ApplicantConverter;
import com.javaweb.jobconnectionsystem.converter.JobPostingConverter;
import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicantResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.repository.*;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private ApplicantConverter applicantConverter ;
    @Autowired
    private BlockUserRepository blockUserRepository;
    @Autowired
    private ApplicantJobTypeRepository applicanJobTypeRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private PhoneNumberRepository phoneRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private JobTypeRepository jobTypeRepository;
    @Autowired
    private JobPostingConverter jobPostingConverter;
    @Override
    public List<ApplicantEntity> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @Override
    public ApplicantEntity saveApplicant(ApplicantDTO applicantDTO) {
        ApplicantEntity applicantEntity = applicantConverter.toApplicantEntity(applicantDTO);
        applicantEntity = applicantRepository.save(applicantEntity);
        return applicantEntity;
    }


    @Override
    public Optional<ApplicantEntity> getApplicantEntityById(Long id) {
        Optional<ApplicantEntity> applicant = applicantRepository.findById(id);
        if (applicant.isEmpty()) {
            return null;
        }
        return applicant;
    }

    @Override
    public ApplicantResponse getApplicantResponseById(Long id) {
        ApplicantEntity applicantEntity = applicantRepository.findById(id).get();
        return applicantConverter.toApplicantResponse(applicantEntity);
    }

    @Override
    public List<JobPostingSearchResponse> getInterestedPostsByApplicantId(Long id) {
        List<JobPostingEntity> jobPostings = applicantRepository.findById(id).get().getInterestedPosts();
        List<JobPostingSearchResponse> jobPostingSearchResponses = new ArrayList<>();
        for (JobPostingEntity jobPosting : jobPostings) {
            jobPostingSearchResponses.add(jobPostingConverter.toJobPostingSearchResponse(jobPosting));
        }
        return jobPostingSearchResponses;
    }

    @Override
    public ApplicantEntity updateApplicant(Long id, ApplicantEntity applicantDetails) {
        ApplicantEntity applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));


        // Thêm các thuộc tính khác nếu có
        return applicantRepository.save(applicant);
    }

    @Override
    public void deleteApplicantById(Long id) {
        ApplicantEntity applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        List<BlockUserEntity> blockUser = new ArrayList<>(applicant.getBlockedUsers().stream().toList());
        for(BlockUserEntity blockUserEntity : blockUser) {
            applicant.getBlockedUsers().remove(blockUserEntity);
            blockUserRepository.delete(blockUserEntity);
        }
        List<SkillEntity> skill =new ArrayList<> (applicant.getSkills());
        for(SkillEntity skillEntity : skill) {
            applicant.getSkills().remove(skillEntity);
            skillRepository.delete(skillEntity);
        }
        List<ApplicantJobtypeEntity> applicantJobType = new ArrayList<>(applicant.getApplicantJobtypeEntities());
        applicant.getApplicantJobtypeEntities().clear();
        for(ApplicantJobtypeEntity applicantJobTypeEntity : applicantJobType){
            applicantJobTypeEntity.setApplicant(null);
            JobTypeEntity job=applicantJobTypeEntity.getJobType();
            job.getApplicantJobtypeEntities().remove(applicantJobTypeEntity);
            applicantJobTypeEntity.setJobType(null);
        }
        applicantRepository.delete(applicant);
        for(ApplicantJobtypeEntity applicantJobTypeEntity : applicantJobType){
            applicanJobTypeRepository.delete(applicantJobTypeEntity);
        }
    }
}