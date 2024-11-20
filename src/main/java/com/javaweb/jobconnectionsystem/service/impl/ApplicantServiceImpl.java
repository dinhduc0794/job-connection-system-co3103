package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.ApplicantConverter;
import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.repository.*;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicantServiceImpl implements ApplicantService {

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
    @Override
    public List<ApplicantEntity> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @Override
    public ApplicantEntity saveApplicant(ApplicantDTO applicantDTO) {
        if(!applicantDTO.getPhoneNumbers().isEmpty()) {
            List<String> phoneNum= applicantDTO.getPhoneNumbers();
            for(String a : phoneNum) {
                if(phoneRepository.existsByPhoneNumber(a)){
                    phoneRepository.deleteByPhoneNumber(a);
                }
            }
        }
        if(!applicantDTO.getEmails().isEmpty()){
            List<String> email = applicantDTO.getEmails();
            for(String a : email){
                if(emailRepository.existsByEmail(a)){
                   emailRepository.deleteByEmail(a);
                }
            }
        }
       ApplicantEntity applicantEntity = applicantConverter.toApplicantEntity(applicantDTO);
        return applicantRepository.save(applicantEntity);
    }
    @Override
    public Optional<ApplicantEntity> getApplicantById(Long id) {
        Optional<ApplicantEntity> applicant = applicantRepository.findById(id);
        if (applicant.isEmpty()) {
            return null;
        }
        return applicant;
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
        for(ApplicantJobtypeEntity applicantJobTypeEntity : applicantJobType){
            applicantJobTypeEntity.getJobType().getApplicantJobtypeEntities().remove(applicantJobTypeEntity);
            applicant.getApplicantJobtypeEntities().remove(applicantJobTypeEntity);

        }
        applicantRepository.delete(applicant);
    }
}
