package com.javaweb.jobconnectionsystem.model.response;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobpostingApplicationResponse {
    private Long applicationId;
    private String status;
    private String email;
    private String phoneNumber;
    private String description;
    private String resume;
    private Long applicantId;
    private String name;
    private String title;
    public JobpostingApplicationResponse(ApplicationEntity entity) {
        this.applicationId = entity.getId();
        this.status = entity.getStatus().name(); // Lấy tên từ Enum
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.description = entity.getDescription();
        this.resume = entity.getResume();
        this.applicantId = entity.getApplicant().getId(); // Lấy applicantId từ quan hệ ManyToOne
        this.name = entity.getApplicant().getLastName() + " " +entity.getApplicant().getFirstName();
        this.title = entity.getJobPosting().getTitle();
    }
}
