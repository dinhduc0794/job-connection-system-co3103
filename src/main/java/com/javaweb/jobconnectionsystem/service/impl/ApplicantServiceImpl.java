package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.ApplicantConverter;
import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.BlockUserRepository;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public List<ApplicantEntity> getAllApplicants() {
        return applicantRepository.findAll();
    }

    @Override
    public ApplicantEntity saveApplicant(ApplicantDTO applicantDTO) {
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
        List<BlockUserEntity> blockUser = applicant.getBlockedUsers().stream().toList();
        for(BlockUserEntity blockUserEntity : blockUser) {
            applicant.getBlockedUsers().remove(blockUserEntity);
            blockUserRepository.delete(blockUserEntity);
        }

        applicantRepository.delete(applicant);
    }
}
