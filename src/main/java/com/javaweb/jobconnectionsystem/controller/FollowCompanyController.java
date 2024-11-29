package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.entity.FollowCompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.FollowCompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.FollowCompanyService;
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
@RequestMapping("/follow-company")
public class FollowCompanyController {
    @Autowired
    FollowCompanyService followCompanyService;

    @PostMapping()
    public ResponseEntity<?> saveFollowCompany(@Valid @RequestBody FollowCompanyDTO followCompanyDTO, BindingResult bindingResult) {
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
            FollowCompanyEntity followCompanyEntity = followCompanyService.saveFollowCompany(followCompanyDTO);
            if (followCompanyEntity == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(followCompanyEntity);
        }
        catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFollowCompany(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            followCompanyService.deleteFollowCompanyById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("FollowCompany has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}

