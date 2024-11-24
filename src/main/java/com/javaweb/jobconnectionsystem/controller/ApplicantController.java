package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/applicants")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    // Endpoint thêm ứng viên
    @PostMapping()
    public ResponseEntity<?> saveApplicant(@Valid @RequestBody ApplicantDTO applicantDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());

                responseDTO.setMessage("Validation failed");
                responseDTO.setDetail(errorMessages);
                return ResponseEntity.badRequest().body(responseDTO);
            }
            ApplicantEntity applicantEntity = applicantService.saveApplicant(applicantDTO);
            if (applicantEntity == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(applicantEntity);
        }
        catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplicant(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            applicantService.deleteApplicantById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Applicant has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}