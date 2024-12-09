package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Table(name = "application")
@Entity
@Getter
@Setter
public class ApplicationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.WAITING;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "resume")
    private String resume;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    @JsonBackReference
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    @JsonBackReference
    private JobPostingEntity jobPosting;
}
