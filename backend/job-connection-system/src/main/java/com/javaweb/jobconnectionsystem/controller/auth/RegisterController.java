package com.javaweb.jobconnectionsystem.controller.auth;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ApplicantService applicantService;

    @PostMapping("/applicant")
    public ResponseEntity<?> saveApplicant(@Valid @RequestBody ApplicantDTO applicantDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(applicantDTO.getId()!=null){
            return ResponseEntity.badRequest().body("No id is sent for new registration");
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
            responseDTO = applicantService.saveApplicant(applicantDTO);
            return ResponseEntity.ok(responseDTO);
        }
        catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
    @PostMapping("/company")
    public ResponseEntity<?> saveCompany(@Valid @RequestBody CompanyDTO companyDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(companyDTO.getId()!=null){
            return ResponseEntity.badRequest().body("No id is sent for new registration");
        }
        try {
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
        } catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
