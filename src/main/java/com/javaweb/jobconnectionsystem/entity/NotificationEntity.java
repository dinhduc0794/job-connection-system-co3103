package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Table(name = "notification")
@Entity
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "datetime")
    private ZonedDateTime time;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference
    private UserEntity user;
}
