package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.ApplicantConverter;
import com.javaweb.jobconnectionsystem.converter.JobPostingConverter;
import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicantPublicResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.repository.*;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private JobPostingConverter jobPostingConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<ApplicantPublicResponse> getAllApplicants() {
        List<ApplicantEntity> applicants = applicantRepository.findAll();
        List<ApplicantPublicResponse> applicantPublicResponses = new ArrayList<>();
        for (ApplicantEntity applicant : applicants) {
            applicantPublicResponses.add(applicantConverter.toApplicantPublicResponse(applicant));
        }
        return applicantPublicResponses;
    }

    @Override
    public ResponseDTO saveApplicant(ApplicantDTO applicantDTO) {
//        String encodedPassword = passwordEncoder.encode(applicantDTO.getPassword());
//        applicantDTO.setPassword(encodedPassword);

        ResponseDTO responseDTO = new ResponseDTO();
        if (applicantDTO.getId() != null) {
            if (!applicantRepository.existsById(applicantDTO.getId())) {
                responseDTO.setMessage("Không tìm thấy ứng viên cần sửa");
                return responseDTO;
            }
            responseDTO.setMessage("Sửa thông tin ứng viên thành công");
        }
        else {
            responseDTO.setMessage("Đăng ký ứng viên mới thành công");
        }
        ApplicantEntity applicantEntity = applicantConverter.toApplicantEntity(applicantDTO);
        responseDTO.setData(applicantEntity);
        return responseDTO;
    }


    @Override
    public ApplicantDTO getApplicantEntityById(Long id) {
        ApplicantEntity applicantEntity = applicantRepository.findById(id).get();
        ApplicantDTO applicantDTO = applicantConverter.toApplicantDTO(applicantEntity);
        return applicantDTO;
    }

    @Override
    public ApplicantPublicResponse getApplicantResponseById(Long id) {
        ApplicantEntity applicantEntity = applicantRepository.findById(id).get();
        return applicantConverter.toApplicantPublicResponse(applicantEntity);
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
        if (applicant.getApplicantJobtypeEntities() != null) applicant.getApplicantJobtypeEntities().clear();
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