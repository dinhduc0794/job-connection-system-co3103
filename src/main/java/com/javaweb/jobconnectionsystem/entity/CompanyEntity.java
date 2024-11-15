package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "company")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "id")
public class CompanyEntity extends UserEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "tax_code", unique = true, nullable = false)
    private String taxCode;
    @Column(name = "number_of_free_post", nullable = false)
    private Long numberOfFreePost = 5L;

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JobPostingEntity> jobPostings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_field",
            joinColumns = @JoinColumn(name = "company_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "field_id", nullable = false))
    private List<FieldEntity> fields = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CompanyRateApplicantEntity> companyRateApplicantEntities = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ApplicantRateCompanyEntity> applicantRateCompanyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<FollowCompanyEntity> followCompanyEntities = new ArrayList<>();
}
