package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicantResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {
    ApplicantEntity saveApplicant(ApplicantDTO applicantDTO);

    // Lấy tất cả ứng viên
    List<ApplicantEntity> getAllApplicants();

    // Lấy ứng viên theo ID
    Optional<ApplicantEntity> getApplicantEntityById(Long id);

    ApplicantResponse getApplicantResponseById(Long id);

    List<JobPostingSearchResponse> getInterestedPostsByApplicantId(Long id);

    // Cập nhật thông tin ứng viên
    ApplicantEntity updateApplicant(Long id, ApplicantEntity applicantDetails);

    // Xóa ứng viên theo ID
    void deleteApplicantById(Long id);
}
