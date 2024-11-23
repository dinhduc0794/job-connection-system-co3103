package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.CertificationEntity;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    // Endpoint thêm chứng chỉ
    @PostMapping
    public ResponseEntity<CertificationEntity> addCertification(@RequestBody CertificationEntity certification) {
        CertificationEntity createdCertification = certificationService.addCertification(certification);
        if (createdCertification == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu chứng chỉ không hợp lệ
        }
        return ResponseEntity.ok(createdCertification); // Trả về chứng chỉ đã thêm
    }

    // Endpoint lấy tất cả chứng chỉ
    @GetMapping
    public ResponseEntity<List<CertificationEntity>> getAllCertifications() {
        List<CertificationEntity> certifications = certificationService.getAllCertifications();
        if (certifications.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có chứng chỉ, trả về 204 No Content
        }
        return ResponseEntity.ok(certifications); // Trả về danh sách chứng chỉ
    }

    // Endpoint lấy chứng chỉ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CertificationEntity> getCertificationById(@PathVariable Long id) {
        Optional<CertificationEntity> certification = certificationService.getCertificationById(id);
        return certification.map(ResponseEntity::ok) // Trả về chứng chỉ nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy chứng chỉ
    }

    // Endpoint cập nhật chứng chỉ
    @PutMapping("/{id}")
    public ResponseEntity<CertificationEntity> updateCertification(@PathVariable Long id, @RequestBody CertificationEntity certificationDetails) {
        try {
            CertificationEntity updatedCertification = certificationService.updateCertification(id, certificationDetails);
            return ResponseEntity.ok(updatedCertification); // Trả về chứng chỉ đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa chứng chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            certificationService.deleteCertificationById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Certification has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
