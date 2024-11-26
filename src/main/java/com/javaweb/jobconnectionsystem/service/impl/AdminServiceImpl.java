package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AdminEntity;
import com.javaweb.jobconnectionsystem.repository.AdminRepository;
import com.javaweb.jobconnectionsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Thêm tài khoản admin mới
    @Override
    public AdminEntity addAdmin(AdminEntity admin) {
        Optional<AdminEntity> existingAdmin = adminRepository.findByUsername(admin.getUsername());
        if (existingAdmin.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Lưu tài khoản admin mới nếu username chưa tồn tại
        return adminRepository.save(admin);
    }

    @Override
    public AdminEntity getAdminByUsername(String username) {
        return adminRepository.findByUsername(username).get();
    }
    // Lấy tất cả tài khoản admin
    @Override
    public List<AdminEntity> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Lấy tài khoản admin theo ID
    @Override
    public Optional<AdminEntity> getAdminById(Long id) {
        AdminEntity admin = adminRepository.findById(id).orElse(null);
        return Optional.ofNullable(admin); // Trả về Optional thay vì null
    }

    // Cập nhật thông tin tài khoản admin
    @Override
    public AdminEntity updateAdmin(Long id, AdminEntity adminDetails) {
        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        if (adminRepository.existsByUsername(adminDetails.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        admin.setUsername(adminDetails.getUsername());
        admin.setPassword(adminDetails.getPassword());

        return adminRepository.save(admin);
    }

    // Xóa tài khoản admin
    @Override
    public void deleteAdmin(Long id) {
        Optional<AdminEntity> admin = adminRepository.findById(id);
        if (admin.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.delete(admin.get());
    }
}
