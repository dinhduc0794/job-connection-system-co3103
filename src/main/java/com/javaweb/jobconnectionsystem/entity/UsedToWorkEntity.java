package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Table(name = "used_to_work")
@Entity
@Getter
@Setter
public class UsedToWorkEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "position")
    private String position;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonBackReference
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private CompanyEntity company;
}

//27 BANG