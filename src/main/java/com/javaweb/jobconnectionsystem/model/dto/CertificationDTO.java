package com.javaweb.jobconnectionsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CertificationDTO {
    private Long id;
    @NotBlank(message = "Certification name is required")
    private String name;
    private String level;
    private String description;
    private String proof;
    private Date startDate;
    private Date endDate;

}
