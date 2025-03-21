package com.javaweb.jobconnectionsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowCompanyDTO {
    private Long id;
    private Long applicantId;
    private Long companyId;
}
