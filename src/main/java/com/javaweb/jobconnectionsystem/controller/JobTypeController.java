package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import com.javaweb.jobconnectionsystem.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobtypes")
public class JobTypeController {

    @Autowired
    private JobTypeService jobTypeService;

    // Endpoint thêm loại công việc
    @PostMapping
    public ResponseEntity<JobTypeEntity> addJobType(@RequestBody JobTypeEntity jobType) {
        JobTypeEntity createdJobType = jobTypeService.addJobType(jobType);
        if (createdJobType == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu loại công việc không hợp lệ
        }
        return ResponseEntity.ok(createdJobType); // Trả về loại công việc đã thêm
    }

    // Endpoint lấy tất cả loại công việc
    @GetMapping
    public ResponseEntity<List<JobTypeEntity>> getAllJobTypes() {
        List<JobTypeEntity> jobTypes = jobTypeService.getAllJobTypes();
        if (jobTypes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có loại công việc, trả về 204 No Content
        }
        return ResponseEntity.ok(jobTypes); // Trả về danh sách loại công việc
    }

    // Endpoint lấy loại công việc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<JobTypeEntity> getJobTypeById(@PathVariable Long id) {
        Optional<JobTypeEntity> jobType = jobTypeService.getJobTypeById(id);
        return jobType.map(ResponseEntity::ok) // Trả về loại công việc nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy loại công việc
    }

    // Endpoint cập nhật loại công việc
    @PutMapping("/{id}")
    public ResponseEntity<JobTypeEntity> updateJobType(@PathVariable Long id, @RequestBody JobTypeEntity jobTypeDetails) {
        try {
            JobTypeEntity updatedJobType = jobTypeService.updateJobType(id, jobTypeDetails);
            return ResponseEntity.ok(updatedJobType); // Trả về loại công việc đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa loại công việc
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobType(@PathVariable Long id) {
        try {
            jobTypeService.deleteJobTypeById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy loại công việc, trả về 404 Not Found
        }
    }
}
