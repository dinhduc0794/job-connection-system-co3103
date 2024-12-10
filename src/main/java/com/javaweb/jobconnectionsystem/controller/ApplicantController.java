package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.SkillDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicantApplicationReponse;
import com.javaweb.jobconnectionsystem.model.response.ApplicantPublicResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.*;
import jakarta.servlet.http.HttpServletResponse;
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
public class ApplicantController {
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CertificationService certificationService;
    @Autowired
    private RateCompanyService rateCompanyService;
    @Autowired
    private SkillService skillService;
    // lay ra tat ca ung vien dang PublicResponse (ko co usn, psw), dung de hien thi tren trang web
    @GetMapping("/public/applicants")
    public ResponseEntity<List<ApplicantPublicResponse>> getAllApplicants() {
        List<ApplicantPublicResponse> applicants = applicantService.getAllApplicants();
        if (applicants.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có ứng viên, trả về 204 No Content
        }
        return ResponseEntity.ok(applicants); // Trả về danh sách ứng viên
    }

    // lay ra ung vien cu the theo id (dang PublicResponse, dung de hien thi khi bam vao ung vien do (ung vien khac xem hoac cong ty xem)
    @GetMapping("/public/applicants/{id}")
    public ResponseEntity<ApplicantPublicResponse> getPublicApplicantById(@PathVariable Long id) {
        ApplicantPublicResponse applicant = applicantService.getApplicantResponseById(id);
        if (applicant == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(applicant);
    }

    // lay ra 1 cong ty cu the dang ApplicantDTO (co usn, psw) -> dung cho chinh cong ty do de xem, sua thong tin
    @GetMapping("/applicants/{id}")
    public ResponseEntity<ApplicantDTO> getApplicantById(@PathVariable Long id) {
        ApplicantDTO applicant = applicantService.getApplicantEntityById(id);
        if (applicant == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(applicant);
    }


    // Endpoint thêm ứng viên
    @PostMapping("/applicants")
    public ResponseEntity<?> saveApplicant(@Valid @RequestBody ApplicantDTO applicantDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(applicantDTO.getId()==null){
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
            ApplicantEntity applicantEntity = applicantService.saveApplicant(applicantDTO);
            if (applicantEntity == null) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(applicantEntity);
        }
        catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @GetMapping("/applicants/{id}/interested-posts")
    public ResponseEntity<?> getInterestedPosts(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<JobPostingSearchResponse> interestedPosts = applicantService.getInterestedPostsByApplicantId(id);
        if (interestedPosts.isEmpty()){
            responseDTO.setMessage("you have no interested post");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            responseDTO.setMessage("interested post");
            responseDTO.setData(interestedPosts);
            return ResponseEntity.ok(responseDTO);
        }
    }


    @PostMapping("/applicants/rate-company")
    public ResponseEntity<?> rateCompany(@Valid @RequestBody RateCompanyDTO rateCompanyDTO, BindingResult bindingResult) {
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
            responseDTO = rateCompanyService.saveRate(rateCompanyDTO);
            return ResponseEntity.ok(responseDTO);
        }
        catch (Exception e){
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @DeleteMapping("/applicants/rate-company/{id}")
    public ResponseEntity<?> deleteRate(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            rateCompanyService.deleteRate(id);
            responseDTO.setMessage("Delete rate successfully");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    // Endpoint thêm chứng chỉ
    @PostMapping("/applicants/certifications")
    public ResponseEntity<CertificationEntity> addCertification(@RequestBody CertificationEntity certification) {
        CertificationEntity createdCertification = certificationService.addCertification(certification);
        if (createdCertification == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu chứng chỉ không hợp lệ
        }
        return ResponseEntity.ok(createdCertification); // Trả về chứng chỉ đã thêm
    }


    // Endpoint cập nhật thông tin ứng viên
    @PutMapping("/applicants/{id}")
    public ResponseEntity<ApplicantEntity> updateApplicant(@PathVariable Long id, @RequestBody ApplicantEntity applicantDetails) {
        try {
            ApplicantEntity updatedApplicant = applicantService.updateApplicant(id, applicantDetails);
            return ResponseEntity.ok(updatedApplicant); // Trả về ứng viên đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: email đã có), trả về lỗi
        }
    }

    // Endpoint xóa ứng viên
    @DeleteMapping("/applicants/{id}")
    public ResponseEntity<?> deleteApplicant(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            applicantService.deleteApplicantById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Applicant has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
    @PostMapping("/applicants/applications")
    public ResponseEntity<?> saveApplications(@RequestBody ApplicationDTO applicationDTO) {
        try {
            // Gọi service để lưu application
            ApplicationEntity savedApplication = applicationService.saveApplication(applicationDTO);

            // Trả về thông tin ứng dụng đã lưu
            return ResponseEntity.ok(savedApplication);
        } catch (RuntimeException e) {
            // Bắt RuntimeException từ service và trả về lỗi 400 với message chi tiết
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Bắt các lỗi không mong muốn khác và trả về lỗi 500
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }
    @GetMapping("/applicants/{id}/applications")
    public ResponseEntity<?> getAllApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ApplicationEntity> applicationByApplicanID = applicationService.getAllApplicationByApplicantId(id);
        if (applicationByApplicanID.isEmpty()){
            responseDTO.setMessage("You have no application");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            List<ApplicantApplicationReponse> applicationResponseDTOs = applicationByApplicanID.stream()
                    .map(entity -> {
                        // Ensure JobPosting is not null before accessing its properties
                        JobPostingEntity jobPosting = entity.getJobPosting();

                        // Create a new ApplicanApplicationReponse with the appropriate fields
                        return new ApplicantApplicationReponse(
                                entity.getId(),
                                entity.getStatus(),
                                entity.getEmail(),
                                entity.getPhoneNumber(),
                                entity.getDescription(),
                                entity.getResume(),
                                jobPosting != null ? jobPosting.getId() : null,  // Use JobPosting ID if not null
                                jobPosting != null ? jobPosting.getTitle() : " "  // Use Title if JobPosting is not null, else default to " "
                        );
                    })
                    .collect(Collectors.toList());
            responseDTO.setMessage("Application with jobposting");
            responseDTO.setData(applicationResponseDTOs);
            return ResponseEntity.ok(responseDTO);
        }
    }
//
    @GetMapping("/applicants/applications/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        ApplicationEntity application = applicationService.getApplicationById(id);
        if (application == null){
            responseDTO.setMessage("Application not found");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            responseDTO.setMessage("Application with jobposting");
            responseDTO.setData(application);
            return ResponseEntity.ok(responseDTO);
        }
    }

    @DeleteMapping("/applicants/application/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            applicationService.getAllApplicationByApplicantId(id);
            responseDTO.setMessage("Delete succesfully");
            responseDTO.setDetail(Collections.singletonList("Application has been deleted"));
            return ResponseEntity.ok(responseDTO);
        }catch(RuntimeException e ){
            responseDTO.setMessage("Can not delete this application");
            responseDTO.setDetail(Collections.singletonList("Something wrong"));
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
