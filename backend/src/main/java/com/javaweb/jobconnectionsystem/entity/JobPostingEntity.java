package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.enums.ScheduleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "jobposting")
@Entity
public class JobPostingEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "schedule")
    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong cơ sở dữ liệu
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
    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong cơ sở dữ liệu
    private LevelEnum level;

    @Column(name = "image")
    private String image;

    @Column(name = "yoe")
    private Long yoe;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = true)
    @JsonBackReference
    private WardEntity ward;

    @ManyToOne
    @JoinColumn(name = "jobtype_id", nullable = true)
    @JsonBackReference
    private JobTypeEntity jobType;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    @JsonBackReference
    private CompanyEntity company;

    @OneToMany(mappedBy = "jobPosting")
    @JsonManagedReference
    private List<ApplicationEntity> applications;

    @ManyToMany(mappedBy = "interestedPosts")
    @JsonIgnore
    private List<ApplicantEntity> applicants = new ArrayList<>();

}
