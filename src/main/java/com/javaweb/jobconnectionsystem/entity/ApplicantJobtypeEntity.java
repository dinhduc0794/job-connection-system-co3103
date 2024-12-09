package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import jakarta.persistence.*;
import lombok.Data;
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
    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong cơ sở dữ liệu
    private LevelEnum level;
    
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonBackReference
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "jobtype_id")
    @JsonBackReference
    private JobTypeEntity jobType;
}
