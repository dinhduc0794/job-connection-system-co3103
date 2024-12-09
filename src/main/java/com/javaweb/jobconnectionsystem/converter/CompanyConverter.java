package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.CompanyDTO;
import com.javaweb.jobconnectionsystem.model.dto.AddressDTO;
import com.javaweb.jobconnectionsystem.model.dto.FieldDTO;
import com.javaweb.jobconnectionsystem.model.response.CompanyPublicResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.repository.*;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public CompanyEntity toCompanyEntity (CompanyDTO companyDTO) {
        // Bước kiểm tra tính hợp lệ của dữ liệu
        if(companyDTO.getPhoneNumbers() != null && !companyDTO.getPhoneNumbers().isEmpty()) {
            for(String phoneNumber : companyDTO.getPhoneNumbers()) {
                if(phoneNumberRepository.existsByPhoneNumber(phoneNumber)) {
                    if(!phoneNumberRepository.findByPhoneNumber(phoneNumber).getUser().getId().equals(companyDTO.getId())) {
                        throw new RuntimeException("Phonenumber " + phoneNumber + " already exists");
                    }
                }
            }
        }
        if(companyDTO.getEmails() != null && !companyDTO.getEmails().isEmpty()) {
            for(String email : companyDTO.getEmails()) {
                if(emailRepository.existsByEmail(email)) {
                    if(emailRepository.findByEmail(email).getUser().getId() != companyDTO.getId()) {
                        throw new RuntimeException("Email " + email + " already exists");
                    }
                }
            }
        }
        if(companyDTO.getTaxCode() != null) {
            CompanyEntity companyFromTaxCode = companyRepository.findByTaxCode(companyDTO.getTaxCode());
            if (companyFromTaxCode != null) {
                if(companyFromTaxCode.getId() != companyDTO.getId()) {
                    throw new RuntimeException("Company tax code already exists");
                }
            }
        }
        if(companyDTO.getName() != null) {
            CompanyEntity companyFromName = companyRepository.findByName(companyDTO.getName());
            if (companyFromName != null) {
                if(companyFromName.getId() != companyDTO.getId()) {
                    throw new RuntimeException("Company name already exists");
                }
            }
        }
        if(companyDTO.getUsername() != null) {
            CompanyEntity companyFromUserName = companyRepository.findByUsername(companyDTO.getUsername());
            if (companyFromUserName != null) {
                if(companyFromUserName.getId() != companyDTO.getId()) {
                    throw new RuntimeException("Username already exists");
                }
            }
        }

        // Sau khi kiểm tra tính hợp lệ của dữ liệu, thực hiện việc chỉnh sửa hoặc tạp mới
        CompanyEntity companyEntity = modelMapper.map(companyDTO, CompanyEntity.class);

        if (companyDTO.getId() != null) {
            // trường hợp chỉnh sửa thông tin
            CompanyEntity existingCompany = companyRepository.findById(companyDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            // thêm lại các thuộc tính không thuộc trường thay đổi thông tin
            // các thuộc tính là thực thể và liên quan
            companyEntity.setJobPostings(existingCompany.getJobPostings());
            companyEntity.setRating(existingCompany.getRating());
            companyEntity.setRateCompanyEntities(existingCompany.getRateCompanyEntities());
            companyEntity.setFollowCompanyEntities(existingCompany.getFollowCompanyEntities());
            companyEntity.setFields(existingCompany.getFields());
            companyEntity.setUsedToWorkEntities(existingCompany.getUsedToWorkEntities());
            companyEntity.setBlockedUsers(existingCompany.getBlockedUsers());
            companyEntity.setBlockingUsers(existingCompany.getBlockingUsers());
            companyEntity.setNotifications(existingCompany.getNotifications());
            companyEntity.setRateApplicantEntities(existingCompany.getRateApplicantEntities());
            // các thuộc tính không phải thực thể
            companyEntity.setRemainingPost(existingCompany.getRemainingPost());
            // xóa hết thuộc tính cũ
            existingCompany.getPhoneNumbers().clear();
            phoneNumberRepository.deleteAll(existingCompany.getPhoneNumbers());
            existingCompany.getEmails().clear();
            emailRepository.deleteAll(existingCompany.getEmails());
//            existingCompany.getWards().removeAll(existingCompany.getWards());
            existingCompany.getFields().removeAll(existingCompany.getFields());
        }
        // các thộc tính nằm ở bảng khác
//        companyRepository.save(companyEntity);
        // PhoneNumber
        if(companyEntity.getPhoneNumbers() != null && !companyEntity.getPhoneNumbers().isEmpty()) {
            companyEntity.getPhoneNumbers().clear();
        }
        List<String> phoneNumbers = companyDTO.getPhoneNumbers();
        if(phoneNumbers != null && !phoneNumbers.isEmpty()) {
            for(String phoneNumber : phoneNumbers) {
                PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
                phoneNumberEntity.setPhoneNumber(phoneNumber);
                phoneNumberEntity.setUser(companyEntity);
//                phoneNumberRepository.save(phoneNumberEntity);
                companyEntity.getPhoneNumbers().add(phoneNumberEntity);
            }
        }
        // Email
        if(companyEntity.getEmails() != null && !companyEntity.getEmails().isEmpty()) {
            companyEntity.getEmails().clear();
        }
        List<String> emails = companyDTO.getEmails();
        if(emails != null && !emails.isEmpty()) {
            for(String email : emails) {
                EmailEntity emailEntity = new EmailEntity();
                emailEntity.setEmail(email);
                emailEntity.setUser(companyEntity);
//                emailRepository.save(emailEntity);
                companyEntity.getEmails().add(emailEntity);
            }
        }
        // Ward
        companyEntity.setSpecificAddress(companyDTO.getSpecificAddress());
        WardEntity wardEntity = wardRepository.findById(companyDTO.getWard().getId()).get();
        companyEntity.setWard(wardEntity);
//        List<AddressDTO> addressWardIds = companyDTO.getAddressWardIds();
//        if (addressWardIds != null && !addressWardIds.isEmpty()) {
//            for (AddressDTO addressWardId : addressWardIds) {
//                String address = addressWardId.getAddress();
//                Long wardId = addressWardId.getWardId();
//                WardEntity wardEntity = wardRepository.findById(wardId).get();
////                companyEntity.getWards().add(wardEntity);
//                companyEntity.setAddress(address);
//            }
//        }

        // Field
        List<FieldDTO> fieldDTOs = companyDTO.getFields();
        if (fieldDTOs != null && !fieldDTOs.isEmpty()) {
            for (FieldDTO fieldDTO : fieldDTOs) {
                FieldEntity fieldEntity = fieldRepository.findById(fieldDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Field not found"));
                companyEntity.getFields().add(fieldEntity);
            }
        }
        return  companyEntity;
    }

    public  CompanyDTO toCompanyDTO(CompanyEntity companyEntity) {
        CompanyDTO companyDTO = modelMapper.map(companyEntity, CompanyDTO.class);

        // Set address
        if(companyEntity.getWard() != null) {
            WardEntity wardEntity = companyEntity.getWard();
            String wardName = wardEntity.getName();
            String cityName = wardEntity.getCity().getName();
            String provinceName = wardEntity.getCity().getProvince().getName();
            String fullAddress = wardName + ", " + cityName + ", " + provinceName;
            if (StringUtils.notEmptyData(companyEntity.getSpecificAddress())) {
                fullAddress = companyEntity.getSpecificAddress() + ", " + fullAddress;
            }
            companyDTO.setFullAddress(fullAddress);
            companyDTO.getWard().setId(wardEntity.getId());
            companyDTO.getWard().setName(wardEntity.getName());
        }

        List<FieldEntity> fieldEntities = companyEntity.getFields();
        if(fieldEntities != null && !fieldEntities.isEmpty()) {
            for(FieldEntity fieldEntity : fieldEntities) {
                FieldDTO fieldDTO = new FieldDTO();
                fieldDTO.setId(fieldEntity.getId());
                fieldDTO.setName(fieldEntity.getName());
                companyDTO.getFields().add(fieldDTO);
            }
        }

        List<PhoneNumberEntity> phoneNumberEntities = companyEntity.getPhoneNumbers();
        if(phoneNumberEntities != null && !phoneNumberEntities.isEmpty()) {
            for(PhoneNumberEntity phoneNumberEntity : phoneNumberEntities) {
                companyDTO.getPhoneNumbers().add(phoneNumberEntity.getPhoneNumber());
            }
        }

        List<EmailEntity> emailEntities = companyEntity.getEmails();
        if(emailEntities != null && !emailEntities.isEmpty()) {
            for(EmailEntity emailEntity : emailEntities) {
                companyDTO.getEmails().add(emailEntity.getEmail());
            }
        }

        return companyDTO;
    }

    public CompanyPublicResponse toCompanyPublicResponse(CompanyEntity companyEntity) {
        CompanyPublicResponse companyPublicResponse = modelMapper.map(companyEntity, CompanyPublicResponse.class);

        // Set addresses
        if (companyEntity.getWard() != null) {
            WardEntity wardEntity = companyEntity.getWard();
            String wardName = wardEntity.getName();
            String cityName = wardEntity.getCity().getName();
            String provinceName = wardEntity.getCity().getProvince().getName();
            String fullAddress = wardName + ", " + cityName + ", " + provinceName;
            if (StringUtils.notEmptyData(companyEntity.getSpecificAddress())) {
                fullAddress = companyEntity.getSpecificAddress() + ", " + fullAddress;
            }
            companyPublicResponse.setFullAddress(fullAddress);
        }

        // Set phone numbers
        if (companyEntity.getPhoneNumbers() != null && !companyEntity.getPhoneNumbers().isEmpty()) {
            List<String> phoneNumbers = companyEntity.getPhoneNumbers().stream()
                    .map(it -> it.getPhoneNumber())
                    .collect(Collectors.toList());
            companyPublicResponse.setPhoneNumbers(phoneNumbers);
        }

        if (companyEntity.getEmails() != null && !companyEntity.getEmails().isEmpty()) {
            List<String> emails = companyEntity.getEmails().stream()
                    .map(it -> it.getEmail())
                    .collect(Collectors.toList());
            companyPublicResponse.setEmails(emails);
        }

        if (companyEntity.getFields() != null && !companyEntity.getFields().isEmpty()) {
            List<String> fields = companyEntity.getFields().stream()
                    .map(it -> it.getName())
                    .collect(Collectors.toList());
            companyPublicResponse.setFields(fields);
        }

        if (companyEntity.getJobPostings() != null && !companyEntity.getJobPostings().isEmpty()) {
            List<JobPostingSearchResponse> jobPostingResponses = companyEntity.getJobPostings().stream()
                    .map(it -> jobPostingConverter.toJobPostingSearchResponse(it))
                    .collect(Collectors.toList());
            companyPublicResponse.setJobPostings(jobPostingResponses);

            // xu li so luong tuyen dung
            List<JobPostingEntity> jobPostingEntities = companyEntity.getJobPostings();
            Long recruitQuantity = 0L;
            for (JobPostingEntity jobPostingEntity : jobPostingEntities) {
                if (jobPostingEntity.getStatus() == true) {
                    recruitQuantity += jobPostingEntity.getNumberOfApplicants();
                }

            }
            companyPublicResponse.setRecruitQuantity(recruitQuantity);
        }

        if (companyEntity.getFollowCompanyEntities() != null && !companyEntity.getFollowCompanyEntities().isEmpty()) {
            companyPublicResponse.setNumberOfFollowers((long) companyEntity.getFollowCompanyEntities().size());
        } else {
            companyPublicResponse.setNumberOfFollowers(0L); // mac dinh 0
        }

        return companyPublicResponse;
//        if (companyEntity.getRateCompanyEntities() != null && !companyEntity.getRateCompanyEntities().isEmpty()) {
//            double totalRating = companyEntity.getRateCompanyEntities().stream()
//                    .filter(it -> it != null && it.getRate() != null) // check null
//                    .map(it -> it.getRate().getValue().doubleValue())  // RateEnum -> Double
//                    .reduce(0.0, Double::sum); // Tính tổng
//            int count = (int) companyEntity.getRateCompanyEntities().stream()
//                    .filter(it -> it != null && it.getRate() != null)
//                    .count(); // dem so luong
//            companyPublicResponse.setRating(count > 0 ? totalRating / count : 0.0);
//        } else {
//            companyPublicResponse.setRating(0.0); // mac dinh 0.0
//        }
    }
}
