package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobpostings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    // Endpoint thêm bài đăng công việc
    @PostMapping
    public ResponseEntity<JobPostingEntity> addJobPosting(@RequestBody JobPostingEntity jobPosting) {
        JobPostingEntity createdJobPosting = jobPostingService.addJobPosting(jobPosting);
        if (createdJobPosting == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu bài đăng công việc không hợp lệ
        }
        return ResponseEntity.ok(createdJobPosting); // Trả về bài đăng công việc đã thêm
    }

    // Endpoint lấy tất cả bài đăng công việc
    @GetMapping
    public ResponseEntity<List<JobPostingEntity>> getAllJobPostings() {
        List<JobPostingEntity> jobPostings = jobPostingService.getAllJobPostings();
        if (jobPostings.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có bài đăng công việc, trả về 204 No Content
        }
        return ResponseEntity.ok(jobPostings); // Trả về danh sách bài đăng công việc
    }

    // Endpoint lấy bài đăng công việc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<JobPostingEntity> getJobPostingById(@PathVariable Long id) {
        Optional<JobPostingEntity> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(ResponseEntity::ok) // Trả về bài đăng công việc nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy bài đăng công việc
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        try {
            jobPostingService.deleteJobPostingById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy bài đăng công việc, trả về 404 Not Found
        }
    }
}
