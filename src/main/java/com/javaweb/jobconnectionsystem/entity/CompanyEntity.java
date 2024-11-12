package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "taxcode", unique = true, nullable = false)
    private String taxCode;
    @Column(name = "numberOfFreePost")
    private Long numberOfFreePost = 5L;

    @OneToMany(mappedBy = "company", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JobPostingEntity> jobPostings;
}
