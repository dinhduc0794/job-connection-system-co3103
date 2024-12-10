package com.javaweb.jobconnectionsystem.model.dto;

import com.javaweb.jobconnectionsystem.model.location.CityDTO;
import com.javaweb.jobconnectionsystem.model.location.ProvinceDTO;
import com.javaweb.jobconnectionsystem.model.location.WardDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CompanyDTO extends PaginationDTO {
    private Long id;
    @NotBlank(message ="Username is required")
    private String username;
    @NotBlank(message ="User password is required")
    private String password;
    private Boolean isActive = true;    // user KHONG dc phep sua
    private String description;        // user dc phep sua
    private Boolean isPublic = true;    // user dc phep sua
    private Boolean isBanned = false;   // user KHONG dc phep sua
    private String image = "bg.jpg";
    private String specificAddress;
    @NotNull(message = "Company address is required")
    private WardDTO ward;   // chieu nhan va tra ve
    private String fullAddress = null;

    @NotBlank(message = "Company name is required")
    private String name;
    @NotBlank(message = "Company tax code is required")
    private String taxCode;
    private Long remainingPost = 10L;
    private List<FieldDTO> fields;
    private Double rating;

    @Size(min = 1, message = "Company email is required")
    @NotNull
    private List<String> emails;

    @Size(min = 1, message = "Company phone number is required")
    @NotNull
    private List<String> phoneNumbers;

//    private List<Long> notificationIds;   // api rieng
//    private List<Long> blockedUserIds;    // api rieng
}
