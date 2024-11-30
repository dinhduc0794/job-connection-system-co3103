package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.enums.StatusEnum;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.AddressDTO;
import com.javaweb.jobconnectionsystem.model.response.CompanyDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.CompanySearchResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.repository.FieldRepository;
import com.javaweb.jobconnectionsystem.repository.WardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CompanyConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JobPostingConverter jobPostingConverter;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private FieldRepository fieldRepository;

    public CompanyEntity toCompanyEntity (CompanyDTO companyDTO) {
        CompanyEntity companyEntity = modelMapper.map(companyDTO, CompanyEntity.class);
        List<AddressDTO> addressWardIds = companyDTO.getAddressWardIds();
        if (addressWardIds != null && !addressWardIds.isEmpty()) {
            for (AddressDTO addressWardId : addressWardIds) {
                String address = addressWardId.getAddress();
                Long wardId = addressWardId.getWardId();
                WardEntity wardEntity = wardRepository.findById(wardId).get();
                // Ví dụ thêm WardEntity vào CompanyEntity (giả sử companyEntity đã được khởi tạo)
                companyEntity.getWards().add(wardEntity); // Cần phương thức `addWard` trong `CompanyEntity`
                companyEntity.setAddress(address);
            }
        }
        List<Long> fieldIds = companyDTO.getFieldIds();
        if (fieldIds != null && !fieldIds.isEmpty()) {
            for (Long fieldId : fieldIds) {
                // Fetch the FieldEntity for each fieldId from the database
                FieldEntity fieldEntity = fieldRepository.findById(fieldId).orElseThrow(() ->
                        new EntityNotFoundException("FieldEntity with ID " + fieldId + " not found"));
                // Add the FieldEntity to the company's field list
                companyEntity.getFields().add(fieldEntity);  // Assuming CompanyEntity has a `fields` collection
            }
        }

        return  companyEntity;
    }

    public CompanySearchResponse toCompanySearchResponse(CompanyEntity companyEntity) {
        CompanySearchResponse companySearchResponse = modelMapper.map(companyEntity, CompanySearchResponse.class);

        if (companyEntity.getWards() != null && !companyEntity.getWards().isEmpty()) {
            List<String> addressList = new ArrayList<>();
            for (WardEntity wardEntity : companyEntity.getWards()) {
                String wardName = wardEntity.getName();

                String cityName = wardEntity.getCity().getName();

                String provinceName = wardEntity.getCity().getProvince().getName();

                String address = wardName + ", " + cityName + ", " + provinceName;
                addressList.add(address);
            }
            companySearchResponse.setAddresses(addressList);
        }

        if (companyEntity.getPhoneNumbers() != null && !companyEntity.getPhoneNumbers().isEmpty()) {
            List<String> phoneNumbers = companyEntity.getPhoneNumbers().stream()
                    .map(it -> it.getPhoneNumber())
                    .collect(Collectors.toList());
            companySearchResponse.setPhoneNumbers(phoneNumbers);
        }

        if (companyEntity.getEmails() != null && !companyEntity.getEmails().isEmpty()) {
            List<String> emails = companyEntity.getEmails().stream()
                    .map(it -> it.getEmail())
                    .collect(Collectors.toList());
            companySearchResponse.setEmails(emails);
        }

        if (companyEntity.getFields() != null && !companyEntity.getFields().isEmpty()) {
            String strField = companyEntity.getFields().stream()
                    .map(it->it.getName())
                    .collect(Collectors.joining(", "));
            companySearchResponse.setFields(strField);
        }

        if (companyEntity.getJobPostings() != null && !companyEntity.getJobPostings().isEmpty()) {
            List<JobPostingEntity> jobPostings = companyEntity.getJobPostings();
            Long recruitQuantity = 0L;
            for (JobPostingEntity jobPostingEntity : jobPostings) {
                if (jobPostingEntity.getStatus() == true) {
                    recruitQuantity += jobPostingEntity.getNumberOfApplicants();
                }

            }
            companySearchResponse.setRecruitQuantity(recruitQuantity);
        }

        if (companyEntity.getRateCompanyEntities() != null && !companyEntity.getRateCompanyEntities().isEmpty()) {
            double totalRating = companyEntity.getRateCompanyEntities().stream()
                    .filter(it -> it != null && it.getRate() != null) // check null
                    .map(it -> it.getRate().getValue().doubleValue())  // RateEnum -> Double
                    .reduce(0.0, Double::sum); // Tính tổng
            int count = (int) companyEntity.getRateCompanyEntities().stream()
                    .filter(it -> it != null && it.getRate() != null)
                    .count(); // dem so luong
            companySearchResponse.setRating(count > 0 ? totalRating / count : 0.0);
        } else {
            companySearchResponse.setRating(0.0); // mac dinh 0.0
        }

        return companySearchResponse;
    }

    public CompanyDetailResponse toCompanyDetailResponse(CompanyEntity companyEntity) {
        CompanyDetailResponse companyDetailResponse = modelMapper.map(companyEntity, CompanyDetailResponse.class);

        // Set addresses
        if (companyEntity.getWards() != null && !companyEntity.getWards().isEmpty()) {
            List<String> addressList = new ArrayList<>();
            for (WardEntity wardEntity : companyEntity.getWards()) {
                String wardName = wardEntity.getName();

                String cityName = wardEntity.getCity().getName();

                String provinceName = wardEntity.getCity().getProvince().getName();

                String address = wardName + ", " + cityName + ", " + provinceName;
                addressList.add(address);
            }
            companyDetailResponse.setAddresses(addressList);
        }

        // Set phone numbers
        if (companyEntity.getPhoneNumbers() != null && !companyEntity.getPhoneNumbers().isEmpty()) {
            List<String> phoneNumbers = companyEntity.getPhoneNumbers().stream()
                    .map(it -> it.getPhoneNumber())
                    .collect(Collectors.toList());
            companyDetailResponse.setPhoneNumbers(phoneNumbers);
        }

        if (companyEntity.getEmails() != null && !companyEntity.getEmails().isEmpty()) {
            List<String> emails = companyEntity.getEmails().stream()
                    .map(it -> it.getEmail())
                    .collect(Collectors.toList());
            companyDetailResponse.setEmails(emails);
        }

        if (companyEntity.getFields() != null && !companyEntity.getFields().isEmpty()) {
            String strField = companyEntity.getFields().stream()
                    .map(it->it.getName())
                    .collect(Collectors.joining(", "));
            companyDetailResponse.setFields(strField);
        }

        if (companyEntity.getJobPostings() != null && !companyEntity.getJobPostings().isEmpty()) {
            List<JobPostingSearchResponse> jobPostings = companyEntity.getJobPostings().stream()
                    .map(it -> jobPostingConverter.toJobPostingSearchResponse(it))
                    .collect(Collectors.toList());
            companyDetailResponse.setJobPostings(jobPostings);
        }

        if (companyEntity.getRateCompanyEntities() != null && !companyEntity.getRateCompanyEntities().isEmpty()) {
            double totalRating = companyEntity.getRateCompanyEntities().stream()
                    .filter(it -> it != null && it.getRate() != null) // check null
                    .map(it -> it.getRate().getValue().doubleValue())  // RateEnum -> Double
                    .reduce(0.0, Double::sum); // Tính tổng
            int count = (int) companyEntity.getRateCompanyEntities().stream()
                    .filter(it -> it != null && it.getRate() != null)
                    .count(); // dem so luong
            companyDetailResponse.setRating(count > 0 ? totalRating / count : 0.0);
        } else {
            companyDetailResponse.setRating(0.0); // mac dinh 0.0
        }

        if (companyEntity.getFollowCompanyEntities() != null && !companyEntity.getFollowCompanyEntities().isEmpty()) {
            companyDetailResponse.setNumberOfFollowers((long) companyEntity.getFollowCompanyEntities().size());
        } else {
            companyDetailResponse.setNumberOfFollowers(0L); // mac dinh 0
        }

        return companyDetailResponse;
    }
}
