package com.javaweb.jobconnectionsystem.entity;

import com.javaweb.jobconnectionsystem.enums.RateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Table(name = "company_rate_applicant")
@Entity
@Getter
@Setter
public class CompanyRateApplicantEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "rate", nullable = false)
    private RateEnum rate;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private ApplicantEntity applicant;
}
