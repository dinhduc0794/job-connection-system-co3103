package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.ProCompanyEntity;

import java.util.List;
import java.util.Optional;

public interface ProCompanyService {

    // Thêm công ty  mới
    public ProCompanyEntity addProCompany(ProCompanyEntity proCompany);

    // Lấy tất cả công ty
    public List<ProCompanyEntity> getAllProCompanies();

    // Lấy công ty  theo ID
    public Optional<ProCompanyEntity> getProCompanyById(Long id);

    // Cập nhật thông tin công ty
    public ProCompanyEntity updateProCompany(Long id, ProCompanyEntity proCompanyDetails);

    // Xóa công ty  theo ID
    public void deleteProCompanyById(Long id);
}
