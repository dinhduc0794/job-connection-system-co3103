package com.javaweb.jobconnectionsystem.model.location;

import com.javaweb.jobconnectionsystem.entity.WardEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WardDTO {
    private Long id;
    private String name;

    public WardDTO(WardEntity ward) {
        this.id = ward.getId();
        this.name = ward.getName();
    }
}