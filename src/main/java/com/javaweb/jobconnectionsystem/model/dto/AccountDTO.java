package com.javaweb.jobconnectionsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private Long id;
    private String username;
    private String password;
    private String newPassword;
}
