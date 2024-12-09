package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CertificationEntity;

import java.util.List;
import java.util.Optional;

public interface CertificationService {
    // Thêm chứng chỉ
    public CertificationEntity addCertification(CertificationEntity certification);

    // Lấy tất cả chứng chỉ
    public List<CertificationEntity> getAllCertifications();

    // Lấy chứng chỉ theo ID
    public Optional<CertificationEntity> getCertificationById(Long id);

    // Cập nhật chứng chỉ
    public CertificationEntity updateCertification(Long id, CertificationEntity certification);

    // Xóa chứng chỉ theo ID
    public void deleteCertificationById(Long id);
}
