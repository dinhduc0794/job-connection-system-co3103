package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class JobPostingSearchResponse {
    private Long id;
    private String description;
    private String schedule;    //Enum
    private String level;    //Enum
    private Long minSalary;
    private Long maxSalary;
    private Boolean status;
    private String image;
    private Double comanyRating;
    private Long numberOfApplicants;
    private Long allowance;
    private String province;
    private String city;
    private String ward;
    private String companyName;
    private String jobType;
    private String skills;
    private String postDate;
    private String emails;
    private String phoneNumbers;
    private String address;
    private ZonedDateTime postedDate;
}
