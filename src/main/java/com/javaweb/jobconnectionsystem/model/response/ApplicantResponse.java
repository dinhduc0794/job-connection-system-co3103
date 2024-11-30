package com.javaweb.jobconnectionsystem.model.response;

import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ApplicantResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isAvailable = true;
    private String description;
    private Boolean isPublic = true;
    private String address;
    private String image;
    private List<Long> wardIds;
    private List<String> phoneNumbers;
    private LocalDate dob;
    private List<String> emails;
    private List<Long> notificationIds;
    private List<Long> blockedUserIds;
    private List<Long> skillIds;
    private List<Long> certificationIds;
}
