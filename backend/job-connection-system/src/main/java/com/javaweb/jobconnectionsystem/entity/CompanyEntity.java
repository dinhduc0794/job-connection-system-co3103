package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "remaining_post", nullable = false)
    private Long remainingPost = 10L;
    @Column(name = "rating")
    private Double rating = 0.0;

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JobPostingEntity> jobPostings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_field",
            joinColumns = @JoinColumn(name = "company_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "field_id", nullable = false))
    @JsonBackReference
    private List<FieldEntity> fields = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RateApplicantEntity> rateApplicantEntities = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RateCompanyEntity> rateCompanyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FollowCompanyEntity> followCompanyEntities = new ArrayList<>();

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UsedToWorkEntity> usedToWorkEntities = new ArrayList<>();
}
