package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class JobPostingDetailResponse {
    private Long id;
    private String description;
    private String schedule;    //Enum
    private String level;    //Enum
    private Long minSalary;
    private Long maxSalary;
    private Boolean status;
    private String image;
    private Long numberOfApplicants;
    private Long allowance;
    private String province;
    private String city;
    private String ward;
    private String companyName;
    private Double comanyRating;
    private String companyImage;
    private String companyField;
    private String jobType;
    private String skills;
    private String emails;
    private String phoneNumbers;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private ZonedDateTime postedDate;
    private Long yoe;
}