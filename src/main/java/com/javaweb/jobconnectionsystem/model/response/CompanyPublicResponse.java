package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyPublicResponse {
    private Long id;
    private String name;
    private String taxCode;
    private Long remainingPost;
    private Double rating;
    private String image;
    private String description;
    private Boolean isPublic;
    private Boolean isBanned;
    private String fullAddress;     // sua cho nay: chi co 1 address
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<JobPostingSearchResponse> jobPostings;
    private List<String> fields;
    private Long numberOfFollowers;
    private Long recruitQuantity;
}
