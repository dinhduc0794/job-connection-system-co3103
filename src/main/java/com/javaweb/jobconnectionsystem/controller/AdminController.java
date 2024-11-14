package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.AdminEntity;
import com.javaweb.jobconnectionsystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Endpoint thêm admin mới
    @PostMapping
    public ResponseEntity<AdminEntity> addAdmin(@RequestBody AdminEntity admin) {
        try {
            AdminEntity createdAdmin = adminService.addAdmin(admin);
            return ResponseEntity.ok(createdAdmin); // Trả về admin đã thêm
        } catch (RuntimeException e) {
            // Bắt lỗi nếu username đã tồn tại và trả về mã lỗi 400
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu tài khoản đã tồn tại
        }
    }

    // Endpoint lấy tất cả admin
    @GetMapping
    public ResponseEntity<List<AdminEntity>> getAllAdmins() {
        List<AdminEntity> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có admin, trả về 204 No Content
        }
        return ResponseEntity.ok(admins); // Trả về danh sách admin
    }

    // Endpoint lấy admin theo ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminEntity> getAdminById(@PathVariable Long id) {
        Optional<AdminEntity> admin = adminService.getAdminById(id);
        return admin.map(ResponseEntity::ok) // Trả về admin nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy admin
    }

    // Endpoint cập nhật admin
    @PutMapping("/{id}")
    public ResponseEntity<AdminEntity> updateAdmin(@PathVariable Long id, @RequestBody AdminEntity adminDetails) {
        try {
            AdminEntity updatedAdmin = adminService.updateAdmin(id, adminDetails);
            return ResponseEntity.ok(updatedAdmin); // Trả về admin đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: username đã tồn tại), trả về lỗi
        }
    }

    // Endpoint xóa admin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy admin, trả về 404 Not Found
        }
    }
}
