package com.javaweb.jobconnectionsystem.model.request;

import com.javaweb.jobconnectionsystem.enums.RateEnum;
import com.javaweb.jobconnectionsystem.model.dto.PaginationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanySearchRequest {
    private Long id;
    private String name;
    private RateEnum minRating;
    private String taxCode;
    List<String> fields;
    private String province;
    private String city;
    private String ward;
}
