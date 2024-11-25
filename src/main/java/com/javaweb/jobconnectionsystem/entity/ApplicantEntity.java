package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "applicant")
@PrimaryKeyJoinColumn(name = "id")
public class ApplicantEntity extends UserEntity{
    @Column(name = "firstname", nullable = false)
    private String firstName;
    @Column(name = "lastname", nullable = false)
    private String lastName;
    @Column(name = "dob")
    private LocalDate dob;
    @Column(name = "resume")
    private String resume;
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @OneToMany(mappedBy = "applicant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CertificationEntity> certifications = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "applicant_skill",
            joinColumns = @JoinColumn(name = "applicant_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "skill_id", nullable = false))
    @JsonManagedReference
    private List<SkillEntity> skills = new ArrayList<>();

    @OneToMany(mappedBy = "applicant" )
    @JsonManagedReference
    private List<ApplicantJobtypeEntity> applicantJobtypeEntities = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ApplicationEntity> applications = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CompanyRateApplicantEntity> companyRateApplicantEntities = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ApplicantRateCompanyEntity> applicantRateCompanyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UsedToWorkEntity> usedToWorkEntities = new ArrayList<>();
}