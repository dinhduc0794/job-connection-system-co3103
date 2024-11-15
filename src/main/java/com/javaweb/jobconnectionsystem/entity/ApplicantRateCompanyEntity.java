package com.javaweb.jobconnectionsystem.entity;

import com.javaweb.jobconnectionsystem.enums.RateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Table(name = "applicant_rate_company")
@Entity
@Getter
@Setter
public class ApplicantRateCompanyEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "rate", nullable = false)
    private RateEnum rate;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

}
