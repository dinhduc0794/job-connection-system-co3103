package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;

import java.util.List;
import java.util.Optional;

public interface ApplicantService {

    // Thêm ứng viên mới
    public ApplicantEntity saveApplicant(ApplicantDTO applicantDTO);

    // Lấy tất cả ứng viên
    public List<ApplicantEntity> getAllApplicants();

    // Lấy ứng viên theo ID
    public Optional<ApplicantEntity> getApplicantById(Long id);

    // Cập nhật thông tin ứng viên
    public ApplicantEntity updateApplicant(Long id, ApplicantEntity applicantDetails);

    // Xóa ứng viên theo ID
    public void deleteApplicantById(Long id);
}
