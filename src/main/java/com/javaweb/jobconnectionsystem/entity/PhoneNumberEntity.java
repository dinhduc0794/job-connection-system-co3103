package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "phonenumber")
@Entity
public class PhoneNumberEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "phonenumber", unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Khóa ngoại trỏ đến UserEntity
    private UserEntity user;
}
