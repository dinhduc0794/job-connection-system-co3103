package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.listener.RateCompanyEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "applicant_rate_company")
@Entity
@Getter
@Setter
@EntityListeners(RateCompanyEntityListener.class)
public class RateCompanyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate", nullable = false)
    private Integer rate;

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
