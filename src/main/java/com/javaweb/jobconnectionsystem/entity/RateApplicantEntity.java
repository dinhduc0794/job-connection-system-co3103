package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.enums.RateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "company_rate_applicant")
@Entity
@Getter
@Setter
public class RateApplicantEntity extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "rate", nullable = false)
    private RateEnum rate;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private CompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    @JsonBackReference
    private ApplicantEntity applicant;
}
