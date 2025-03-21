package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateCompanyDTO {
    private Long id;
    @NotNull(message = "Applicant ID is required")
    private Long applicantId;
    @NotNull(message = "Company ID is required")
    private Long companyId;
    private String companyName;
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rate;
    private String feedback;
}
