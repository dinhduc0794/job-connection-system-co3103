package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.RateEnum;
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
    @NotNull(message = "Rating is required")
    private RateEnum rate;
    private String feedback;
}
