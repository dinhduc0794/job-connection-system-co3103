package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.enums.ScheduleEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPostingDTO {
    private Long id;
    private String description;
    private Long allowance;
    private Long maxSalary;
    private Long minSalary;
    private List<String> skills;
    private String jobType;
    private String image;
    private Long numberOfApplicants;
    private LevelEnum level;
    private ScheduleEnum schedule;
    private Long companyId;
    private Long wardId;
}
