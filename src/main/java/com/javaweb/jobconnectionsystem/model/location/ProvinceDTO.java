package com.javaweb.jobconnectionsystem.model.location;

import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProvinceDTO {
    private Long id;
    private String name;
    private List<CityDTO> cities;

    public ProvinceDTO(ProvinceEntity province) {
        this.id = province.getId();
        this.name = province.getName();
        this.cities = province.getCities().stream().map(CityDTO::new).collect(Collectors.toList());
    }
}
