package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkillDTO {
    private Long id;
    private String name;
    public SkillDTO( Long id, String name ) {
        this.id = id;
        this.name = name;
    }
}
