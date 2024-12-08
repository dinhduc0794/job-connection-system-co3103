package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobTypeDTO {
    private LevelEnum level;
    private Long jobTypeId;
}
