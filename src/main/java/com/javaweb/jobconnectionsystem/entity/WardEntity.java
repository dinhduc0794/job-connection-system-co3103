package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
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
    @JoinColumn(name = "city_id", nullable = true)
    @JsonBackReference
    private CityEntity city;

    // Quan hệ với UserEntity: Một phường/xã có thể có nhiều người dùng sinh sống
    @ManyToMany(mappedBy = "wards", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<UserEntity> users;

    // Quan hệ với JobPostingEntity: Một phường/xã có thể có nhiều bài đăng tuyển dụng
    @OneToMany(mappedBy = "ward", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<JobPostingEntity> jobPostings;
}
