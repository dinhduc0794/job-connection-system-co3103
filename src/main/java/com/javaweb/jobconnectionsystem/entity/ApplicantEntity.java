package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "applicant")
@Entity
public class ApplicantEntity extends UserEntity{
    @Column(name = "firstname", nullable = false)
    private String firstName;
    @Column(name = "lastname", nullable = false)
    private String lastName;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "resume")
    private String resume;
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @OneToMany(mappedBy = "applicant", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CertificationEntity> certifications = new ArrayList<>();
}
