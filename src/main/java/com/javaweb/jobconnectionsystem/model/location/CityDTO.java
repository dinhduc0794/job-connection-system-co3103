package com.javaweb.jobconnectionsystem.model.location;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CityDTO {
    private Long id;
    private String name;
    private List<WardDTO> wards;

    public CityDTO(CityEntity city) {
        this.id = city.getId();
        this.name = city.getName();
        this.wards = city.getWards().stream().map(WardDTO::new).collect(Collectors.toList());
    }
}
