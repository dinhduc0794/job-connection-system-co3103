package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "interested_post")
@Entity
@Getter
@Setter
public class InterestedPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    @JsonBackReference
    private ApplicantEntity applicant;

    @ManyToOne
    @JoinColumn(name = "jobposting_id")
    @JsonBackReference
    private JobPostingEntity jobPosting;
}
