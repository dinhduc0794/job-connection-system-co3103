package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "\"user\"")
@Entity
@Inheritance(strategy = InheritanceType.JOINED )
@PrimaryKeyJoinColumn(name = "id")
public class UserEntity extends AccountEntity{
    @Column(name = "description")
    private String description;

    @Column(name = "isPublic")
    private Boolean isPublic = true;

    @Column(name = "is_banned")
    private Boolean isBanned = false;

    // 1 user can have many phone numbers
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNumberEntity> phoneNumbers;

    // 1 user can have many email
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailEntity> emails ;

    // 1 user can have many notification
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationEntity> notifications;
}
