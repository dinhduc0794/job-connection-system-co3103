package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "phonenumber")
@Entity
public class PhoneNumberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phonenumber", unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // Khóa ngoại trỏ đến UserEntity
    @JsonBackReference
    private UserEntity user;
}
