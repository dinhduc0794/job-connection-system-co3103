package com.javaweb.jobconnectionsystem.model.response;
import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicanApplicationReponse {

    private Long id;
    private StatusEnum status;
    private String email;
    private String phoneNumber;
    private String description;
    private String resume;
    private Long jobPostingId;
    private String title;
    public ApplicanApplicationReponse(Long id, StatusEnum status, String email, String phoneNumber, String description, String resume, Long jobPostingId,String title) {
        this.id = id;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.resume = resume;
        this.title=title;
        this.jobPostingId = jobPostingId;
    }
    public ApplicanApplicationReponse(){};

    // Getters and Setters
}
