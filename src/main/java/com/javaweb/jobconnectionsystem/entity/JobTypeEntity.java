package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private Long name;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private FieldEntity field;

    @OneToMany(mappedBy = "jobType", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<SkillEntity> skills;
}
