package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Table(name = "block")
@Entity
@Getter
@Setter
public class BlockUserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_id", nullable = false)
    @JsonBackReference
    private UserEntity blocker;  // User who blocks another user

    @ManyToOne
    @JoinColumn(name = "blocked_user_id", nullable = false)
    @JsonBackReference
    private UserEntity blockedUser;  // User who is blocked
}
