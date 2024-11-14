package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    // Kiểm tra xem tên đăng nhập đã tồn tại chưa
    public boolean existsByUsername(String username);

    // Tìm tài khoản admin theo tên đăng nhập
    public Optional<AdminEntity> findByUsername(String username);
}
