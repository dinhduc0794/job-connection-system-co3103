package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.RateEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateCompanyDTO {
    private Long applicantId;
    private Long companyId;
    private RateEnum rate;      // Điểm đánh giá (ví dụ: 1-5)
    private String comment;  // Nhận xét (nếu có)
}
