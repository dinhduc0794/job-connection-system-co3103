package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobTypeDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class JobTypeController {

    @Autowired
    private JobTypeService jobTypeService;


    // Endpoint lấy tất cả loại công việc
    @GetMapping("/jobtypes")
    public ResponseEntity<List<JobTypeEntity>> getAllJobTypes() {
        List<JobTypeEntity> jobTypes = jobTypeService.getAllJobTypes();
        if (jobTypes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có loại công việc, trả về 204 No Content
        }
        return ResponseEntity.ok(jobTypes); // Trả về danh sách loại công việc
    }

    @GetMapping("/public/jobtypes")
    public ResponseEntity<List<JobTypeDTO>> getAllJobTypeDTOs() {
        List<JobTypeDTO> jobTypes = jobTypeService.getAllJobTypeDTOs();
        if (jobTypes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có loại công việc, trả về 204 No Content
        }
        return ResponseEntity.ok(jobTypes); // Trả về danh sách loại công việc
    }

    // Endpoint lấy loại công việc theo ID
    @GetMapping("/public/jobtypes/{id}")
    public ResponseEntity<JobTypeEntity> getJobTypeById(@PathVariable Long id) {
        Optional<JobTypeEntity> jobType = jobTypeService.getJobTypeById(id);
        return jobType.map(ResponseEntity::ok) // Trả về loại công việc nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy loại công việc
    }

    // Endpoint thêm loại công việc
    @PostMapping("/jobtypes")
    public ResponseEntity<JobTypeEntity> addJobType(@RequestBody JobTypeEntity jobType) {
        JobTypeEntity createdJobType = jobTypeService.addJobType(jobType);
        if (createdJobType == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu loại công việc không hợp lệ
        }
        return ResponseEntity.ok(createdJobType); // Trả về loại công việc đã thêm
    }

    // Endpoint cập nhật loại công việc
    @PutMapping("/jobtypes/{id}")
    public ResponseEntity<JobTypeEntity> updateJobType(@PathVariable Long id, @RequestBody JobTypeEntity jobTypeDetails) {
        try {
            JobTypeEntity updatedJobType = jobTypeService.updateJobType(id, jobTypeDetails);
            return ResponseEntity.ok(updatedJobType); // Trả về loại công việc đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa loại công việc
    @DeleteMapping("/jobtypes/{id}")
    public ResponseEntity<?> deleteJobType(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            jobTypeService.deleteJobTypeById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Job type has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
