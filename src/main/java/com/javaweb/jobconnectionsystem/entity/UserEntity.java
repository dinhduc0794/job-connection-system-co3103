package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "\"user\"")
@Entity
@Inheritance(strategy = InheritanceType.JOINED )
@PrimaryKeyJoinColumn(name = "id")
public class UserEntity extends AccountEntity {
    @Column(name = "description")
    private String description;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "is_banned")
    private Boolean isBanned = false;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image = "bg.jpg";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_ward",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "ward_id", nullable = false))
    @JsonBackReference
    private List<WardEntity> wards = new ArrayList<>();

    // 1 user can have many phone numbers

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PhoneNumberEntity> phoneNumbers = new ArrayList<>();

    // 1 user can have many email

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EmailEntity> emails = new ArrayList<>();

    // 1 user can have many notification

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<NotificationEntity> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "blocker",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BlockUserEntity> blockedUsers = new ArrayList<>(); // Danh sách các mối quan hệ block (User đã block)

    @OneToMany(mappedBy = "blockedUser",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BlockUserEntity> blockingUsers = new ArrayList<>(); // Danh sách các mối quan hệ block (User bị block)

    @OneToMany(mappedBy = "reporter",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReportEntity> reportedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReportEntity> reportingUsers = new ArrayList<>();
}