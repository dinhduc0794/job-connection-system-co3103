package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ApplicationDTO {
    private Long id;
    private StatusEnum status = StatusEnum.WAITING;
    private String description;
    @NotBlank(message="Email is required")
    private String email;
    @NotBlank(message="Phone number is required")
    private String phoneNumber;
    @NotBlank(message="Resume is reqxuired")
    private String resume;
    @NotNull(message="ApplicantId is required")
    private Long applicantId;
    @NotNull(message="JobPostingId is required")
    private Long jobPostingId;
}