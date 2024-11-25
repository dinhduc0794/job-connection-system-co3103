package com.javaweb.jobconnectionsystem.entity;

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
@Table(name = "province")
public class ProvinceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Quan hệ với CityEntity: Một tỉnh/thành phố có thể có nhiều quận/huyện
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CityEntity> cities = new ArrayList<>();
}
