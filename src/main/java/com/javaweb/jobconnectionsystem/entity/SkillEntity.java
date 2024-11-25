package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "skill")
@Entity
public class SkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "jobtype_id", nullable = true)
    @JsonBackReference
    private JobTypeEntity jobType;

    @ManyToMany(mappedBy="skills", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ApplicantEntity> applicants = new ArrayList<>();
}