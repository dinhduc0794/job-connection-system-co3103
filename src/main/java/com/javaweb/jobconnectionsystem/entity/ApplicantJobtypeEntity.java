package com.javaweb.jobconnectionsystem.entity;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Table(name = "applicant_jobtype")
@Entity
@Getter
@Setter
public class ApplicantJobtypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level")
    private LevelEnum level;
    
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "jobtype_id")
    private JobTypeEntity jobType;
}
