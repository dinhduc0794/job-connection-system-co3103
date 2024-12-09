package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobTypeDTO {
    private Long id;
    private String name;
    private LevelEnum level;
}
