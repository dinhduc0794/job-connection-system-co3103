package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.*;
import com.javaweb.jobconnectionsystem.service.ApplicationService;
import com.javaweb.jobconnectionsystem.service.CompanyService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping("/public/companies")
    public ResponseEntity<List<CompanySearchResponse>> getCompaniesByConditions(@ModelAttribute CompanySearchRequest params) {
        List<CompanySearchResponse> companyResponses = companyService.getAllCompanies(params);

        if (companyResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyResponses);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable Long id) {
        Optional<CompanyEntity> company = companyService.getCompanyEntityById(id);
        return company.map(ResponseEntity::ok) // Trả về ứng viên nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy ứng viên
    }


    @GetMapping("/public/companies/{id}")
    public ResponseEntity<?> getPublicCompanyById(@PathVariable Long id) {
        CompanyDetailResponse companyDetail = companyService.getCompanyDetailResponseById(id);
        if (companyDetail == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyDetail);
    }

    // Endpoint thêm công ty
    @PostMapping("/companies")
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyDTO companyDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(companyDTO.getId()==null){
            return ResponseEntity.badRequest().body("sửa thì id của tao đâu ");
        }
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
            CompanyEntity companyEntity = companyService.saveCompany(companyDTO);
            if (companyEntity == null) {
                return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu bài đăng công việc không hợp lệ
            }
            return ResponseEntity.ok(companyEntity); // Trả về bài đăng công việc đã thêm
        }
        catch (Exception e){
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }


    // Endpoint cập nhật công ty
    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyEntity> updateCompany(@PathVariable Long id, @RequestBody CompanyEntity companyDetails) {
        try {
            CompanyEntity updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok(updatedCompany); // Trả về công ty đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: tên công ty đã tồn tại), trả về lỗi
        }
    }

    // Endpoint xóa công ty
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            companyService.deleteCompanyById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Company has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @GetMapping("/companies/jobpostings/{id}/applications")
    public ResponseEntity<?> getApplicationByJobpostingId(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ApplicationEntity> application = applicationService.getAllApplicationByJobpostingId(id);
        if (application == null){
            responseDTO.setMessage("application not found");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            responseDTO.setMessage("application with jobposting");
            responseDTO.setData(application);
            return ResponseEntity.ok(responseDTO);
        }
    }
    @PostMapping("/companies/jobpostings")
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
    @PostMapping("/companies/jobpostings/applications")
    //sửa trạng thái
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
    @DeleteMapping("/companies/jobpostings/{id}/applications")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            applicationService.deleteApplicationByJobpostingId(id);
            responseDTO.setMessage("delete succesfully");
            responseDTO.setDetail(Collections.singletonList("application has been deleted"));
            return ResponseEntity.ok(responseDTO);
        }catch(RuntimeException e ){
            responseDTO.setMessage("canot delete this application");
            responseDTO.setDetail(Collections.singletonList("this application is not in status Rejected"));
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
