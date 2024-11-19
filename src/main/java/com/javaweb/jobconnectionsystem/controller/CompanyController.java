package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    // Endpoint thêm công ty
    @PostMapping
    public ResponseEntity<CompanyEntity> saveCompany(@Valid @RequestBody CompanyDTO companyDTO, BindingResult bindingResult) {
        CompanyEntity createdCompany = companyService.saveCompany(companyDTO);
        if (createdCompany == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu công ty đã tồn tại
        }
        return ResponseEntity.ok(createdCompany); // Trả về công ty đã thêm
    }

    // Endpoint lấy tất cả công ty
    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có công ty, trả về 204 No Content
        }
        return ResponseEntity.ok(companies); // Trả về danh sách công ty
    }

    // Endpoint lấy công ty theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable Long id) {
        Optional<CompanyEntity> company = companyService.getCompanyById(id);
        return company.map(ResponseEntity::ok) // Trả về công ty nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy công ty
    }

    // Endpoint cập nhật công ty
    @PutMapping("/{id}")
    public ResponseEntity<CompanyEntity> updateCompany(@PathVariable Long id, @RequestBody CompanyEntity companyDetails) {
        try {
            CompanyEntity updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok(updatedCompany); // Trả về công ty đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: tên công ty đã tồn tại), trả về lỗi
        }
    }

    // Endpoint xóa công ty
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompanyById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy công ty, trả về 404 Not Found
        }
    }
}
