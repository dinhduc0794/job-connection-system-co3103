package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "jobposting")
@Entity
public class JobPostingEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule")
    private String schedule;

    @Column(name = "minSalary")
    private Long minSalary;

    @Column(name = "maxSalary")
    private Long maxSalary;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;
}
