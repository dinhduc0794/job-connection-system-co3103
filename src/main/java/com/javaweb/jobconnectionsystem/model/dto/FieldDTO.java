package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldDTO {
    private Long id;
    private String name;

    public FieldDTO(FieldEntity field) {
        this.id = field.getId();
        this.name = field.getName();
    }

    public FieldDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
