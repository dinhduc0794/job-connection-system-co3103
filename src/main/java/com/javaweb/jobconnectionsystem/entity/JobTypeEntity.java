package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "jobtype")
@Entity
public class JobTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "jobType")
    @JsonManagedReference
    private List<ApplicantJobtypeEntity> applicantJobtypeEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    @JsonBackReference
    private FieldEntity field;

    @OneToMany(mappedBy = "jobType", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SkillEntity> skills;

    @OneToMany(mappedBy = "jobType")
    @JsonManagedReference
    private List<JobPostingEntity> jobPostings;
}
