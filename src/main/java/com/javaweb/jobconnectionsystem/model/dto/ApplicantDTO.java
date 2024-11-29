package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class ApplicantDTO {
    private Long id;
    @NotBlank(message ="Username is required")
    private String username;
    @NotBlank(message ="User password is required")
    private String password;
    @NotBlank(message = "Person firt name is required ")
    private String firstName;
    @NotBlank(message ="Person last name is required")
    private String lastName;
    private Boolean isAvailable = true;

    private String description;
    private Boolean isPublic = true;
    private String address;

    private String image;
    @Size(min = 1, message = "Address is required")
    private List<Long> wardIds;
    @Size(min = 1, message = "Applicant phone number is required")
    private List<String> phoneNumbers;
    @NotNull(message = "Day of birth is required")
    private LocalDate dob;
    @Size(min = 1, message = "Applicant email is required")
    private List<String> emails;
    private List<Long> notificationIds;
    private List<Long> blockedUserIds;
    private List<Long> skillIds;
    private List<CertificationDTO> certifications;
}
