package com.javaweb.jobconnectionsystem.entity;

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
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private WardEntity ward;
}
