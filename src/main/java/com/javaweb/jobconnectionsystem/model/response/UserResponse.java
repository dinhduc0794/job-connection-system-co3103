package com.javaweb.jobconnectionsystem.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private Boolean isActive;
    private Boolean isBanned;
    private Boolean isPublic;
    private String description;
    private String image;
    private String fullAddress;
    private List<String> emails;
    private List<String> phoneNumbers;
    private String role;
}
