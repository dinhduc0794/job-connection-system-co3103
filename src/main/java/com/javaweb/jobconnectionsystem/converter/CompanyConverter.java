package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.CompanyDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.CompanySearchResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.repository.WardRepository;
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

    public CompanyEntity toCompanyEntity (CompanyDTO companyDTO) {
        CompanyEntity companyEntity = modelMapper.map(companyDTO, CompanyEntity.class);
        Map<String, Long> addressWardIds = companyDTO.getAddressWardIds();
        if (addressWardIds != null && !addressWardIds.isEmpty()) {
            for (Map.Entry<String, Long> entry : addressWardIds.entrySet()) {
                String address = entry.getKey();
                Long wardId = entry.getValue();
                WardEntity wardEntity = wardRepository.findById(wardId).get();
                // Ví dụ thêm WardEntity vào CompanyEntity (giả sử companyEntity đã được khởi tạo)
                companyEntity.getWards().add(wardEntity); // Cần phương thức `addWard` trong `CompanyEntity`
                companyEntity.setAddress(address);
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

        if (companyEntity.getApplicantRateCompanyEntities() != null && !companyEntity.getApplicantRateCompanyEntities().isEmpty()) {
            double totalRating = companyEntity.getApplicantRateCompanyEntities().stream()
                    .filter(it -> it != null && it.getRate() != null) // check null
                    .map(it -> it.getRate().getValue().doubleValue())  // RateEnum -> Double
                    .reduce(0.0, Double::sum); // Tính tổng
            int count = (int) companyEntity.getApplicantRateCompanyEntities().stream()
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
