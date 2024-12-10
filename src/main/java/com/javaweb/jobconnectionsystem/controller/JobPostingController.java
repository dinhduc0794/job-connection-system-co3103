package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.ApplicationService;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/public/jobpostings/all")
    public ResponseEntity<List<JobPostingSearchResponse>> getAllJobPostings() {
        List<JobPostingSearchResponse> jobPostings = jobPostingService.getAllJobPostings();
        if (jobPostings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/public/jobpostings/all/active")
    public ResponseEntity<List<JobPostingSearchResponse>> getAllActiveJobPostings() {
        List<JobPostingSearchResponse> jobPostings = jobPostingService.getAllActiveJobPostings();
        if (jobPostings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPostings);
    }

    // Endpoint lấy tất cả bài đăng công việc theo nhiều tiêu chí
    @GetMapping("/public/jobpostings")
    public ResponseEntity<?> getJobPostingsByConditions(@ModelAttribute JobPostingSearchRequest params) {
        List<JobPostingSearchResponse> jobPostings = jobPostingService.getAllJobPostings(params, PageRequest.of(params.getPage() - 1, params.getMaxPageItems()));

        int totalItems = jobPostingService.countTotalItems(params);
        int totalPage = (int) Math.ceil((double) totalItems / params.getMaxPageItems());

        JobPostingSearchResponse response = new JobPostingSearchResponse();
        response.setListResult(jobPostings);
        response.setTotalItem(jobPostingService.countTotalItems(params));
        response.setTotalPage();

        if (jobPostings.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có bài đăng công việc, trả về 204 No Content
        }
            return ResponseEntity.ok(response); // Trả về danh sách bài đăng công việc
    }

    @GetMapping("/public/jobpostings/{id}")
    public ResponseEntity<JobPostingDetailResponse> getJobPostingById(@PathVariable Long id) {
        JobPostingDetailResponse jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPosting);
    }
}
