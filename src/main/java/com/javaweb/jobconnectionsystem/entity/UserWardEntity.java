package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user_ward")
@Entity
@Getter
@Setter
public class UserWardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    @JsonBackReference
    private WardEntity ward;
}
