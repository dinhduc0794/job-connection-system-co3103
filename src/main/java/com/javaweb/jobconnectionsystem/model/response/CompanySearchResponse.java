package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanySearchResponse {
    private Long id;
    private String name;
    private Double rating;
    private String taxCode;
    private List<String> addresses;
    private List<String> phoneNumbers;
    private List<String> emails;
    private String description;
    private String image;
    private String fields;
    private Long recruitQuantity;
}
