package com.javaweb.jobconnectionsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyDTO extends PaginationDTO {
    private Long id;

    @NotBlank(message ="Username is required")
    private String username;

    @NotBlank(message ="User password is required")
    private String password;

    @NotBlank(message = "Company name is required")
    private String name;

    @NotBlank(message = "Company tax code is required")
    private String taxCode;
    private List<Long> fieldIds;
    private Double rating;
    private Long remainingPost;
    private String image = "bg.jpg";

    @Size(min = 1, message = "Address is required")
    List<AddressDTO> addressWardIds;

    private String description;
    private Boolean isPublic;
    private Boolean isBanned;
    private Boolean isActive;

    @Size(min = 1, message = "Company email is required")
    private List<String> emails;

    @Size(min = 1, message = "Company phone number is required")
    private List<String> phoneNumbers;
    private List<Long> notificationIds;
    private List<Long> blockedUserIds;
}
