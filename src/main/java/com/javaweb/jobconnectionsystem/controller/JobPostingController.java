package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.InterestedPostService;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobpostings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private InterestedPostService interestedPostService;

    // Endpoint lấy tất cả bài đăng công việc theo nhiều tiêu chí
    @GetMapping
    public ResponseEntity<List<JobPostingSearchResponse>> getJobPostingsByConditions(@ModelAttribute JobPostingSearchRequest params) {
        List<JobPostingSearchResponse> jobPostings = jobPostingService.getAllJobPostings(params);
        if (jobPostings.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có bài đăng công việc, trả về 204 No Content
        }
            return ResponseEntity.ok(jobPostings); // Trả về danh sách bài đăng công việc
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingDetailResponse> getJobPostingById(@PathVariable Long id) {
        JobPostingDetailResponse jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosting);
    }

    // Endpoint thêm bài đăng công việc
    @PostMapping
    public ResponseEntity<?> saveJobPosting(@Valid @RequestBody JobPostingDTO jobPostingDTO, BindingResult bindingResult) {
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
            // neu dung thi //xuong service -> xuong repo -> save vao db
            JobPostingEntity jobPostingEntity = jobPostingService.saveJobPosting(jobPostingDTO);
            if (jobPostingEntity == null) {
                return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu bài đăng công việc không hợp lệ
            }
            return ResponseEntity.ok(jobPostingEntity); // Trả về bài đăng công việc đã thêm
        }
        catch (Exception e){
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    // Endpoint cập nhật bài đăng công việc
    @PutMapping("/{id}")
    public ResponseEntity<JobPostingEntity> updateJobPosting(@PathVariable Long id, @RequestBody JobPostingEntity jobPostingDetails) {
        try {
            JobPostingEntity updatedJobPosting = jobPostingService.updateJobPosting(id, jobPostingDetails);
            return ResponseEntity.ok(updatedJobPosting); // Trả về bài đăng công việc đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa bài đăng công việc
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJobPosting(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            jobPostingService.deleteJobPostingById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Job posting has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
