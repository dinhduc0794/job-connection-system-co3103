package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicantPublicResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {
    ResponseDTO saveApplicant(ApplicantDTO applicantDTO);

    // Lấy tất cả ứng viên
    List<ApplicantPublicResponse> getAllApplicants();

    // Lấy ứng viên theo ID
    ApplicantDTO getApplicantEntityById(Long id);

    ApplicantPublicResponse getApplicantResponseById(Long id);

    // Cập nhật thông tin ứng viên
    ApplicantEntity updateApplicant(Long id, ApplicantEntity applicantDetails);

    // Xóa ứng viên theo ID
    void deleteApplicantById(Long id);
}
