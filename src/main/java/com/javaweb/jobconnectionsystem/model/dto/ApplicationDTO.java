package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
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
    private String phoneNum;
    private String email;
    @NotBlank(message="resume is required")
    private String resume;
    private Long applicantId;
    private Long jobPostingId;
}