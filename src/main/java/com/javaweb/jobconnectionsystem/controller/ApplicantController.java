package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applicants")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    // Endpoint thêm ứng viên
    @PostMapping
    public ResponseEntity<ApplicantEntity> addApplicant(@RequestBody ApplicantEntity applicant) {
        ApplicantEntity createdApplicant = applicantService.addApplicant(applicant);
        return ResponseEntity.ok(createdApplicant); // Trả về ứng viên đã thêm
    }

    // Endpoint lấy tất cả ứng viên
    @GetMapping
    public ResponseEntity<List<ApplicantEntity>> getAllApplicants() {
        List<ApplicantEntity> applicants = applicantService.getAllApplicants();
        if (applicants.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có ứng viên, trả về 204 No Content
        }
        return ResponseEntity.ok(applicants); // Trả về danh sách ứng viên
    }

    // Endpoint lấy ứng viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApplicantEntity> getApplicantById(@PathVariable Long id) {
        Optional<ApplicantEntity> applicant = applicantService.getApplicantById(id);
        return applicant.map(ResponseEntity::ok) // Trả về ứng viên nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy ứng viên
    }

    // Endpoint cập nhật thông tin ứng viên
    @PutMapping("/{id}")
    public ResponseEntity<ApplicantEntity> updateApplicant(@PathVariable Long id, @RequestBody ApplicantEntity applicantDetails) {
        try {
            ApplicantEntity updatedApplicant = applicantService.updateApplicant(id, applicantDetails);
            return ResponseEntity.ok(updatedApplicant); // Trả về ứng viên đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: email đã có), trả về lỗi
        }
    }

    // Endpoint xóa ứng viên
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApplicant(@PathVariable Long id) {
        try {
            applicantService.deleteApplicantById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy ứng viên, trả về 404 Not Found
        }
    }
}
