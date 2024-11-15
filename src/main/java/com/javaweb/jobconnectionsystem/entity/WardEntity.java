package com.javaweb.jobconnectionsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ward")
public class WardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Quan hệ với CityEntity (quận/huyện)
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    // Quan hệ với UserEntity: Một phường/xã có thể có nhiều người dùng sinh sống
    @ManyToMany(mappedBy = "wards", fetch = FetchType.LAZY)
    private List<UserEntity> users;
}
