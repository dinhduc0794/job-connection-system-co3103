package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.InterestedPostEntity;
import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.InterestedPostDTO;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.InterestedPostService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interested-post")
public class InterestedPostController {
    @Autowired
    private InterestedPostService interestedPostService;

    @PostMapping()
    public ResponseEntity<?> rateApplicant(@Valid @RequestBody InterestedPostDTO interestedPostDTO, BindingResult bindingResult) {
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
            InterestedPostEntity interestedPostEntity = interestedPostService.saveInterestedPost(interestedPostDTO);
            if (interestedPostEntity == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(interestedPostEntity);
        }
        catch (Exception e){
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteRate(@PathVariable Long id) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        try {
//            interestedPostService.deleteInterestedPost(id);
//            responseDTO.setMessage("Delete interested post successfully");
//            return ResponseEntity.ok(responseDTO);
//        } catch (Exception e) {
//            responseDTO.setMessage("Internal server error");
//            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
//        }
//    }
}