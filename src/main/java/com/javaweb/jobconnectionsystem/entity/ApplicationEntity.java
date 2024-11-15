package com.javaweb.jobconnectionsystem.entity;

import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Table(name = "application")
@Entity
@Getter
@Setter
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.WAITING;

    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @Column(name = "description")
    private String description;

    @Column(name = "resume")
    private String resume;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPostingEntity jobPosting;
}
