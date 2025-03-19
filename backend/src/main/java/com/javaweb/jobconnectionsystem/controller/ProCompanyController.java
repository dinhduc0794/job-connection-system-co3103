package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ProCompanyEntity;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.ProCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProCompany(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            proCompanyService.deleteProCompanyById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Pro company has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
