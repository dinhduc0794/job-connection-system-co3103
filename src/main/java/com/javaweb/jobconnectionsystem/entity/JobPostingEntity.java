package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.enums.ScheduleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "jobposting")
@Entity
public class JobPostingEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule")
    private ScheduleEnum schedule;

    @Column(name = "min_salary")
    private Long minSalary;

    @Column(name = "max_salary")
    private Long maxSalary;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "number_of_applicants")
    private Long numberOfApplicants;

    @Column(name = "allowance")
    private Long allowance;

    @Column(name = "level")
    private LevelEnum level;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "jobtype_id", nullable = true)
    @JsonBackReference
    private JobTypeEntity jobType;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    @JsonBackReference
    private CompanyEntity company;

    @OneToMany(mappedBy = "jobPosting")
    @JsonBackReference
    private List<PostDateEntity> postDates;

    @OneToMany(mappedBy = "jobPosting")
    @JsonBackReference
    private List<ApplicationEntity> applications;
}
