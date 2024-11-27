package com.javaweb.jobconnectionsystem.model.response;

import com.javaweb.jobconnectionsystem.model.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class JobPostingSearchResponse extends AbstractDTO {
    private Long id;
    private String title;
    private String schedule;    //Enum
    private String level;    //Enum
    private Long minSalary;
    private Long maxSalary;
    private Boolean status;
    private String province;
    private String city;
    private String ward;
    private Long companyId;
    private String companyName;
    private String companyImage;
    private String jobType;
    private String skills;
    private String yoe;
    private String field;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
