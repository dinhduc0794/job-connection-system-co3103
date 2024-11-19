package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message ="User PassWord is required")
    private String password;
    private Boolean isActive;
    @NotBlank(message = "Person firt name is required ")
    private String firstName;
    @NotBlank(message ="Person last name is required")
    private String lastName;
    private Boolean isAvailable;

    private String description;
    private Boolean isPublic;
    private Boolean isBanned;
    private String address;

    private String image;
    @NotNull(message = "Person ward is required")
    private List<Long> wardIds;
    @NotBlank(message ="Phone number is required")
    private List<String> phoneNumberIds;
    @NotNull(message = "Day of birth is required")
    private LocalDate dob;
    @NotBlank(message="Person email is required")
    private List<String> emailIds;
    private List<Long> notificationIds;
    private List<Long> blockedUserIds;


}
