package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.*;
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
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanySearchResponse>> getCompaniesByConditions(@ModelAttribute CompanySearchRequest params) {
        List<CompanySearchResponse> companyResponses = companyService.getAllCompanies(params);

        int totalItems = companyService.countTotalItems(params);
        int totalPage = (int) Math.ceil((double) totalItems / params.getMaxPageItems());

        CompanySearchResponse response = new CompanySearchResponse();
        response.setListResult(companyResponses);
        response.setTotalItems(totalItems);
        response.setTotalPage(totalPage);

        if (companyResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        CompanyDetailResponse companyDetail = companyService.getCompanyById(id);
        if (companyDetail == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyDetail);
    }

    // Endpoint thêm công ty
    @PostMapping
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
    @PutMapping("/{id}")
    public ResponseEntity<CompanyEntity> updateCompany(@PathVariable Long id, @RequestBody CompanyEntity companyDetails) {
        try {
            CompanyEntity updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok(updatedCompany); // Trả về công ty đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: tên công ty đã tồn tại), trả về lỗi
        }
    }

    // Endpoint xóa công ty
    @DeleteMapping("/{id}")
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
}
