package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.response.*;
import com.javaweb.jobconnectionsystem.service.ApplicationService;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import com.javaweb.jobconnectionsystem.service.RateCompanyService;
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
    @Autowired
    private RateCompanyService rateCompanyService;

    // lay tat ca cac cong ty duoi dang CompanyPublicResponse (khong co usn, psw) -> dung cho trang tim kiem cong ty
    @GetMapping("/public/companies")
    public ResponseEntity<List<CompanyPublicResponse>> getCompaniesByConditions(@ModelAttribute CompanySearchRequest params) {
        List<CompanyPublicResponse> companyResponses = companyService.getAllCompanies(params);

        if (companyResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyResponses);
    }

    // lay ra 1 cong ty cu the dang CompanyPublicResponse (khong co usn, psw) -> dung cho trang xem chi tiet cong ty (khi click vao 1 cong ty)
    @GetMapping("/public/companies/{id}")
    public ResponseEntity<?> getPublicCompanyById(@PathVariable Long id) {
        CompanyPublicResponse companyDetail = companyService.getCompanyDetailResponseById(id);
        if (companyDetail == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyDetail);
    }

    // lay ra 1 cong ty cu the dang CompanyDTO (co usn, psw) -> dung cho chinh cong ty do de xem, sua thong tin
    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        CompanyDTO companyDTO = companyService.getCompanyById(id);
        if (companyDTO == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyDTO);
    }

    // Endpoint thêm công ty
    @PostMapping("/companies")
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyDTO companyDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(companyDTO.getId()==null){
            return ResponseEntity.badRequest().body("Id is required to update a company");
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
            responseDTO = companyService.saveCompany(companyDTO);
            return ResponseEntity.ok(responseDTO);
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


    // get all rate của 1 company có id = 2
    @GetMapping("/companies/{id}/rates")
    public ResponseEntity<?> getRateCompanyByApplicantId(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<RateCompanyDTO> rateCompanyList = rateCompanyService.getRateCompanyByCompanyId(id);
        if (rateCompanyList == null || rateCompanyList.isEmpty()) {
            responseDTO.setMessage("Rate not found");
            return ResponseEntity.ok(responseDTO);
        } else {
            responseDTO.setMessage("Get rate of company successfully");
            responseDTO.setData(rateCompanyList);
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
            responseDTO = jobPostingService.saveJobPosting(jobPostingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        }
        catch (Exception e){
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    // lay ra tat ca cac don ung tuyen cua 1 bai dang cong viec
    @GetMapping("/companies/jobpostings/{id}/applications")
    public ResponseEntity<?> getApplicationByJobpostingId(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ApplicationEntity> applications = applicationService.getAllApplicationByJobpostingId(id);

        if (applications == null || applications.isEmpty()) {
            responseDTO.setMessage("Application not found");
            return ResponseEntity.ok(responseDTO);
        } else {
            List<JobpostingApplicationResponse> applicationResponses = applications.stream()
                    .map(JobpostingApplicationResponse::new)
                    .collect(Collectors.toList());

            responseDTO.setMessage("Get application of jobpost successfully");
            responseDTO.setData(applicationResponses);
            return ResponseEntity.ok(responseDTO);
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

    @DeleteMapping("/companies/jobpostings/applications/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            applicationService.deleteApplicationById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Application has been deleted"));
            return ResponseEntity.ok(responseDTO);
        }
        catch(RuntimeException e ){
            responseDTO.setMessage("Can not delete this application");
            responseDTO.setDetail(Collections.singletonList("This application is not in status Rejected"));
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/companies/jobpostings/{id}")
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
