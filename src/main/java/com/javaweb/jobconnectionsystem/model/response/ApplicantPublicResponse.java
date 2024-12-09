package com.javaweb.jobconnectionsystem.model.response;

import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplicationAotProcessor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ApplicantPublicResponse {
    // public
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isAvailable = true;
    private String image;
    private String description;
    private Boolean isPublic = true;
    private String fullAddress;
    private LocalDate dob;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<String> skills;
    private List<String> jobTypes;
    private List<CertificationDTO> certifications;
}
