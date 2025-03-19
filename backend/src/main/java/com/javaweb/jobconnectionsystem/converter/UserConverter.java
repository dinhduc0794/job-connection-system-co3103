package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.response.UserResponse;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ApplicantRepository applicantRepository;

    public UserResponse convertToResponse(UserEntity userEntity) {
        // custom mapping
        UserResponse userResponse = new UserResponse();
        if (companyRepository.existsById(userEntity.getId())) {
            CompanyEntity companyEntity = companyRepository.getReferenceById(userEntity.getId());
            userResponse.setId(companyEntity.getId());
            userResponse.setUsername(companyEntity.getUsername());
            userResponse.setIsActive(companyEntity.getIsActive());
            userResponse.setIsPublic(companyEntity.getIsPublic());
            userResponse.setDescription(companyEntity.getDescription());
            userResponse.setImage(companyEntity.getImage());
            userResponse.setRole("company");
            userResponse.setName(companyEntity.getName());
        }
        else if (applicantRepository.existsById(userEntity.getId())) {
            ApplicantEntity applicantEntity = applicantRepository.getReferenceById(userEntity.getId());
            userResponse.setId(applicantEntity.getId());
            userResponse.setUsername(applicantEntity.getUsername());
            userResponse.setIsActive(applicantEntity.getIsActive());
            userResponse.setIsPublic(applicantEntity.getIsPublic());
            userResponse.setDescription(applicantEntity.getDescription());
            userResponse.setImage(applicantEntity.getImage());
            userResponse.setRole("applicant");
            userResponse.setName(applicantEntity.getFirstName() + " " + applicantEntity.getLastName());
        }

        if (userEntity.getEmails() != null && !userEntity.getEmails().isEmpty()) {
            List<String> emails = userEntity.getEmails().stream()
                    .map(EmailEntity::getEmail)
                    .collect(Collectors.toList());
            userResponse.setEmails(emails);
        }

        if (userEntity.getPhoneNumbers() != null && !userEntity.getPhoneNumbers().isEmpty()) {
            List<String> phones = userEntity.getPhoneNumbers().stream()
                    .map(PhoneNumberEntity::getPhoneNumber)
                    .collect(Collectors.toList());
            userResponse.setPhoneNumbers(phones);
        }

        if (userEntity.getWard() != null) {
            WardEntity wardEntity = userEntity.getWard();
            String wardName = wardEntity.getName();
            String cityName = wardEntity.getCity().getName();
            String provinceName = wardEntity.getCity().getProvince().getName();
            String fullAddress = wardName + ", " + cityName + ", " + provinceName;
            if (StringUtils.notEmptyData(userEntity.getSpecificAddress())) {
                fullAddress = userEntity.getSpecificAddress() + ", " + fullAddress;
            }
            userResponse.setFullAddress(fullAddress);
        }

        return userResponse;
    }
}
