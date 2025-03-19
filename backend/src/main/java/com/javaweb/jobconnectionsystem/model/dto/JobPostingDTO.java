package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.enums.ScheduleEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPostingDTO extends PaginationDTO {
    private Long id;

    @NotBlank(message = "Job description is required")
    private String description;
    private Long allowance;

    @NotBlank(message = "Job title is required")
    private String title;

    @NotNull(message = "Salary must not be null")
    @Min(value = 0, message = "Salary must not be negative")
    private Long maxSalary;

    @NotNull(message = "Salary must not be null")
    @Min(value = 0, message = "Salary must not be negative")
    private Long minSalary;

    private Long yoe;
    private Boolean status = true;

    @Size(min = 1, message = "Skill is required")
    private List<String> skills;

    // thieu validate
    private Long jobTypeId;
    private String image;

    @NotNull(message = "Number of applicants is required")
    private Long numberOfApplicants;
    private LevelEnum level;

    @NotNull(message = "Schedule is required")
    private ScheduleEnum schedule;

    @NotNull(message = "Company is required")
    private Long companyId;

    // thieu validate
    private Long wardId;
}
