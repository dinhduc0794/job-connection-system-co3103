package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.enums.RateEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "applicant_rate_company")
@Entity
@Getter
@Setter
public class RateCompanyEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "rate", nullable = false)
    private RateEnum rate;

    @Column(name = "feedback")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    @JsonBackReference
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private CompanyEntity company;
}
