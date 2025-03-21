package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.model.location.WardDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ApplicantDTO {
    private Long id;
    @NotBlank(message ="Username is required")
    private String username;
    @NotBlank(message ="User password is required")
    private String password;
    private Boolean isActive = true;    // user KHONG dc phep sua
    private String description;
    private Boolean isPublic = true;    // user dc phep sua
    private Boolean isBanned = false;   // user KHONG dc phep sua
    private Boolean isAvailable = true; // user dc phep sua
    private String image = "bg.jpg";
    private String specificAddress;
    @NotNull(message = "Applicant address is required")
    private WardDTO ward;   // chieu nhan va tra ve
    private String fullAddress = null;

    @NotBlank(message = "First name is required ")
    private String firstName;
    @NotBlank(message ="Last name is required")
    private String lastName;

    @Size(min = 1, message = "Applicant email is required")
    @NotNull
    private List<String> emails;

    @Size(min = 1, message = "Applicant phone number is required")
    @NotNull
    private List<String> phoneNumbers;

    private LocalDate dob;

    private List<JobTypeDTO> jobTypes;
    private List<SkillDTO> skills;
    private List<CertificationDTO> certifications;

//    private List<Long> notificationIds;   // api rieng
//    private List<Long> blockedUserIds;    // api rieng
}
