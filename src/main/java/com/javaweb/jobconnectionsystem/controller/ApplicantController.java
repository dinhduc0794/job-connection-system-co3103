package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.SkillDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicanApplicationReponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.*;
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

    // Endpoint lấy ứng viên theo ID
    @GetMapping("/public/applicants/{id}")
    public ResponseEntity<ApplicantEntity> getApplicantById(@PathVariable Long id) {
        Optional<ApplicantEntity> applicant = applicantService.getApplicantById(id);
        return applicant.map(ResponseEntity::ok) // Trả về ứng viên nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy ứng viên
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

    // Endpoint lấy tất cả ứng viên
    @GetMapping("/public/applicants")
    public ResponseEntity<List<ApplicantEntity>> getAllApplicants() {
        List<ApplicantEntity> applicants = applicantService.getAllApplicants();
        if (applicants.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có ứng viên, trả về 204 No Content
        }
        return ResponseEntity.ok(applicants); // Trả về danh sách ứng viên
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
    @PostMapping("/applicants/certification")
    public ResponseEntity<CertificationEntity> addCertification(@RequestBody CertificationEntity certification) {
        CertificationEntity createdCertification = certificationService.addCertification(certification);
        if (createdCertification == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu chứng chỉ không hợp lệ
        }
        return ResponseEntity.ok(createdCertification); // Trả về chứng chỉ đã thêm
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
    @GetMapping("/applicants/{id}/applications")
    public ResponseEntity<?> getAllApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        List<ApplicationEntity> applicationByApplicanID = applicationService.getAllApplicationByApplicantId(id);
        if (applicationByApplicanID.isEmpty()){
            responseDTO.setMessage("you have no application");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            List<ApplicanApplicationReponse> applicationResponseDTOs = applicationByApplicanID.stream()
                    .map(entity -> {
                        // Ensure JobPosting is not null before accessing its properties
                        JobPostingEntity jobPosting = entity.getJobPosting();

                        // Create a new ApplicanApplicationReponse with the appropriate fields
                        return new ApplicanApplicationReponse(
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
            responseDTO.setMessage("application with jobposting");
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
            responseDTO.setMessage("application not found");
            return ResponseEntity.ok(responseDTO);
        }
        else {
            responseDTO.setMessage("application with jobposting");
            responseDTO.setData(application);
            return ResponseEntity.ok(responseDTO);
        }
    }

    @DeleteMapping("/applicants/application/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            applicationService.getAllApplicationByApplicantId(id);
            responseDTO.setMessage("delete succesfully");
            responseDTO.setDetail(Collections.singletonList("application has been deleted"));
            return ResponseEntity.ok(responseDTO);
        }catch(RuntimeException e ){
            responseDTO.setMessage("canot delete this application");
            responseDTO.setDetail(Collections.singletonList("some thing wrong"));
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    // Endpoint thêm kỹ năng
    @PostMapping("/applicants/skills")
    public ResponseEntity<?> saveSkill(@Valid @RequestBody SkillDTO skillDTO, BindingResult bindingResult) {
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
            SkillEntity skillEntity = skillService.saveSkill(skillDTO);
            if (skillEntity == null) {
                return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu bài đăng công việc không hợp lệ
            }
            return ResponseEntity.ok(skillEntity); // Trả về bài đăng công việc đã thêm
        }
        catch (Exception e){
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    // Endpoint lấy tất cả kỹ năng
    @GetMapping("/applicants/skills")
    public ResponseEntity<List<SkillEntity>> getAllSkills() {
        List<SkillEntity> skills = skillService.getAllSkills();
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có kỹ năng, trả về 204 No Content
        }
        return ResponseEntity.ok(skills); // Trả về danh sách kỹ năng
    }

    // Endpoint lấy kỹ năng theo ID
    @GetMapping("/applicants/skills/{id}")
    public ResponseEntity<SkillEntity> getSkillById(@PathVariable Long id) {
        Optional<SkillEntity> skill = skillService.getSkillById(id);
        return skill.map(ResponseEntity::ok) // Trả về kỹ năng nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy kỹ năng
    }

    // Endpoint xóa kỹ năng
    @DeleteMapping("/applicants/skills/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            skillService.deleteSkillById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Skill has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
