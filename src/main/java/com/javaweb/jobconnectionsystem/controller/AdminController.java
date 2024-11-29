package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.response.CompanySearchResponse;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private CompanyService companyService;
    @GetMapping("/applicants")
    public ResponseEntity<List<ApplicantEntity>> getAllApplicants() {
        List<ApplicantEntity> applicants = applicantService.getAllApplicants();
        if (applicants.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có ứng viên, trả về 204 No Content
        }
        return ResponseEntity.ok(applicants); // Trả về danh sách ứng viên
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanySearchResponse>> getCompaniesByConditions(@ModelAttribute CompanySearchRequest params) {
        List<CompanySearchResponse> companyResponses = companyService.getAllCompanies(params);

        if (companyResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyResponses);
    }
}
