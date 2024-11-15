package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Table(name = "block")
@Entity
@Getter
@Setter
public class BlockUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "blocker_id", nullable = false)
    private UserEntity blocker;  // User who blocks another user

    @ManyToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    private UserEntity blockedUser;  // User who is blocked
}
