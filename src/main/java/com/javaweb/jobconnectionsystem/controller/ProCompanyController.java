package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ProCompanyEntity;
import com.javaweb.jobconnectionsystem.service.ProCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/procompanies")
public class ProCompanyController {

    @Autowired
    private ProCompanyService proCompanyService;

    // Endpoint thêm công ty
    @PostMapping
    public ResponseEntity<ProCompanyEntity> addProCompany(@RequestBody ProCompanyEntity proCompany) {
        ProCompanyEntity createdProCompany = proCompanyService.addProCompany(proCompany);
        return ResponseEntity.ok(createdProCompany); // Trả về công ty  đã thêm
    }

    // Endpoint lấy tất cả công ty
    @GetMapping
    public ResponseEntity<List<ProCompanyEntity>> getAllProCompanies() {
        List<ProCompanyEntity> proCompanies = proCompanyService.getAllProCompanies();
        if (proCompanies.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có công ty, trả về 204 No Content
        }
        return ResponseEntity.ok(proCompanies); // Trả về danh sách công ty
    }

    // Endpoint lấy công ty  theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProCompanyEntity> getProCompanyById(@PathVariable Long id) {
        Optional<ProCompanyEntity> proCompany = proCompanyService.getProCompanyById(id);
        return proCompany.map(ResponseEntity::ok) // Trả về công ty nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy công ty
    }

    // Endpoint cập nhật thông tin công ty c
    @PutMapping("/{id}")
    public ResponseEntity<ProCompanyEntity> updateProCompany(@PathVariable Long id, @RequestBody ProCompanyEntity proCompanyDetails) {
        try {
            ProCompanyEntity updatedProCompany = proCompanyService.updateProCompany(id, proCompanyDetails);
            return ResponseEntity.ok(updatedProCompany); // Trả về công ty  đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: mã số thuế đã có), trả về lỗi
        }
    }

    // Endpoint xóa công ty
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProCompany(@PathVariable Long id) {
        try {
            proCompanyService.deleteProCompanyById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy công ty, trả về 404 Not Found
        }
    }
}
