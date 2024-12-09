package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "follow_company")
@Entity
@Getter
@Setter
public class FollowCompanyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private CompanyEntity company;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonBackReference
    private ApplicantEntity applicant;
}
