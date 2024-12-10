package com.javaweb.jobconnectionsystem.model.response;

import com.javaweb.jobconnectionsystem.model.dto.PaginationDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobPostingSearchResponse extends PaginationDTO {
    private Long id;
    private String title;
    private String schedule;    //Enum
    private String level;    //Enum
    private Long minSalary;
    private Long maxSalary;
    private Boolean status;
    private Long allowance;
    private String province;
    private String city;
    private String ward;
    private Long companyId;
    private String companyName;
    private String companyImage;
    private String jobType;
    private String skills;
    private String yoe;
    private Long numberOfApplicants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
