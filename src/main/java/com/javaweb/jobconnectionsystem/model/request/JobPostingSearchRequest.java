package com.javaweb.jobconnectionsystem.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPostingSearchRequest {
    private String id;
    private String schedule;
    private Long salary;
    private String jobType;
    private String companyName;
    private String province;
    private String city;
    private String ward;
    private Integer comanyRating;
    private Long minOfApplicants;
    private Long allowance;
    List<String> skills;
}
