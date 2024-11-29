package com.javaweb.jobconnectionsystem.model.request;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.enums.ScheduleEnum;
import com.javaweb.jobconnectionsystem.model.dto.PaginationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPostingSearchRequest extends PaginationDTO {
    private Long id;
    private String title;
    private ScheduleEnum schedule;
    private LevelEnum level;
    private Long yoe;
    private Long salary;
    private String jobType;
    private String companyName;
    private String province;
    private String city;
    private String ward;
    private Double comanyRating;
    private Long minOfApplicants;
    private Long allowance;
    List<String> skills;
}
