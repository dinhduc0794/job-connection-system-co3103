package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ApplicationDTO {
    private int id;
    private StatusEnum status;
    private ZonedDateTime datetime;
    private String description;
    @NotBlank(message="resume is required")
    private String resume;
    private Long applicantId;
    private Long jobPostingId;
}