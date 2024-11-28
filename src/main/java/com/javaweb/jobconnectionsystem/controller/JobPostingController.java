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
import com.javaweb.jobconnectionsystem.service.InterestedPostService;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

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
    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/all")
    public ResponseEntity<List<JobPostingSearchResponse>> getAllJobPostings() {
        List<JobPostingSearchResponse> jobPostings = jobPostingService.getAllJobPostings();
        if (jobPostings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobPostings);
    }

    // Endpoint lấy tất cả bài đăng công việc theo nhiều tiêu chí
    @GetMapping
    public ResponseEntity<List<JobPostingSearchResponse>> getJobPostingsByConditions(@ModelAttribute JobPostingSearchRequest params) {
        List<JobPostingSearchResponse> jobPostings = jobPostingService.getAllJobPostings(params, PageRequest.of(params.getPage() - 1, params.getMaxPageItems()));

        int totalItems = jobPostingService.countTotalItems(params);
        int totalPage = (int) Math.ceil((double) totalItems / params.getMaxPageItems());

        JobPostingSearchResponse response = new JobPostingSearchResponse();
        response.setListResult(jobPostings);
        response.setTotalItems(totalItems);
        response.setTotalPage(totalPage);


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

    @PostMapping("/application")
    public ResponseEntity<?> saveApplication(@Valid @RequestBody ApplicationDTO applicationDTO, BindingResult bindingResult) {
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
            ApplicationEntity applicationEntity = applicationService.saveApplication(applicationDTO);
            if (applicationEntity == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(applicationEntity);
        }
        catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<?> getAllApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ApplicationEntity> applicationByJobposting = applicationService.getAllApplicationByJobpostingId(id);
        if ( applicationByJobposting.isEmpty()){
            responseDTO.setMessage("you have no application");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            responseDTO.setMessage("application with applicant");
            responseDTO.setData(applicationByJobposting);
            return ResponseEntity.ok(responseDTO);
        }
    }

    @DeleteMapping("/application/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            applicationService.DeleteApplicationByJobposting(id);
            responseDTO.setMessage("delete succesfully");
            responseDTO.setDetail(Collections.singletonList("application has been deleted"));
            return ResponseEntity.ok(responseDTO);
        }catch(RuntimeException e ){
            responseDTO.setMessage("canot delete this application");
            responseDTO.setDetail(Collections.singletonList("this application is not in status Rejected"));
            return ResponseEntity.badRequest().body(responseDTO);
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
