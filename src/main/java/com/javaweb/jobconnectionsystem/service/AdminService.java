package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.AdminEntity;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    // Thêm tài khoản admin
    public AdminEntity addAdmin(AdminEntity admin);

    // Lấy tất cả tài khoản admin
    public List<AdminEntity> getAllAdmins();

    // Lấy tài khoản admin theo ID
    public Optional<AdminEntity> getAdminById(Long id);

    // Cập nhật thông tin admin
    public AdminEntity updateAdmin(Long id, AdminEntity adminDetails);

    // Xóa tài khoản admin
    public void deleteAdmin(Long id);
    public AdminEntity getAdminByUsername(String username);
}
