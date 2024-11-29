package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkillDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private Long jobTypeId;
    private List<Long> applicantIds;
}
