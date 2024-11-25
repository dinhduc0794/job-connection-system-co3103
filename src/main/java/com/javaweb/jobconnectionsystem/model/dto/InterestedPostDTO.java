package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterestedPostDTO {
    private Long id;
    @NotNull(message = "Applicant ID is required")
    private Long applicantId;
    @NotNull(message = "Job Posting ID is required")
    private Long jobPostingId;
}
