package com.javaweb.jobconnectionsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Quan hệ với ProvinceEntity (Tỉnh/Thành phố)
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    @JsonBackReference
    private ProvinceEntity province;

    // Quan hệ với WardEntity: Một quận/huyện có thể có nhiều phường/xã
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<WardEntity> wards= new ArrayList<>();
}
