package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "report")
@Entity
@Getter
@Setter
public class ReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    @JsonBackReference
    private UserEntity reporter;  // User who report another user

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    @JsonBackReference
    private UserEntity reportedUser;  // User who is reported

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonBackReference
    private AdminEntity admin;
}
