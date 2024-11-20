package com.javaweb.jobconnectionsystem.model.response;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.entity.NotificationEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CompanyDetailResponse {
    private Long id;
    private String name;
    private String taxCode;
    private Long numberOfFreePost;
    private String image;
    private String description;
    private Boolean isPublic;
    private Boolean isBanned;
    private List<String> addresses;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<JobPostingSearchResponse> jobPostings;
    private String fields;
    private Double rating;
    private Long numberOfFollowers;
}
