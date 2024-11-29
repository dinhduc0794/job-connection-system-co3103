package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String role ;
    private String token;
    private Long id;
}
