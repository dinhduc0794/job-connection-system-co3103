package com.javaweb.jobconnectionsystem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class BlockingDTO {
    Long id ;
    Long blockerId;
    Long blockedUserId;
}
