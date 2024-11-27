package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.ApplicantService;
import com.javaweb.jobconnectionsystem.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("**/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping()
    public ResponseEntity<?> saveApplication(@Valid @RequestBody ApplicationDTO applicationDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
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
            ApplicationEntity applicationEntity = applicationService.saveApplication(applicationDTO);
            if (applicationEntity == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(applicationEntity);
        } catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<?> getAllApplication(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<ApplicationEntity> applicationByApplicanID = applicationService.getAllApplicationByApplicantId(id);
        List<ApplicationEntity> applicationByJobposting = applicationService.getAllApplicationByJobpostingId(id);
        if (applicationByApplicanID.isEmpty() && applicationByJobposting.isEmpty()) {
            responseDTO.setMessage("you have no application");
            return ResponseEntity.ok(responseDTO);
        } else if (applicationByApplicanID.isEmpty()) {
            responseDTO.setMessage("application with jobposting");
            responseDTO.setData(applicationByJobposting);
            return ResponseEntity.ok(responseDTO);
        } else if (applicationByJobposting.isEmpty()) {
            responseDTO.setMessage("application with applicant");
            responseDTO.setData(applicationByApplicanID);
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.badRequest().body("Server error");
    }
}

