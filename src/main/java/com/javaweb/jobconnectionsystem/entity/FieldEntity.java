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
@Table(name = "field")
@Entity
public class FieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "field", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JobTypeEntity> jobTypes;

    @ManyToMany(mappedBy="fields", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<CompanyEntity> companies = new ArrayList<>();
}
