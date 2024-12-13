package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.model.dto.*;
import com.javaweb.jobconnectionsystem.model.location.WardDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicantApplicationReponse;
import com.javaweb.jobconnectionsystem.model.response.ApplicantPublicResponse;
import com.javaweb.jobconnectionsystem.repository.*;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicantConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private BlockUserRepository blockUserRepository;
    @Autowired
    private JobTypeRepository jobTypeRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private ApplicantJobTypeRepository applicantJobTypeRepository;
    @Autowired
    private CertificationRepository certificationRepository;

    public ApplicantEntity toApplicantEntity(ApplicantDTO applicantDTO) {
        // Bước kiểm tra tính hợp lệ của dữ liệu


        // Sau khi kiểm tra tính hợp lệ của dữ liệu, thực hiện việc chỉnh sửa hoặc tạp mới
        ApplicantEntity applicantEntity = modelMapper.map(applicantDTO, ApplicantEntity.class);

        if (applicantDTO.getId() != null) {
            // trường hợp chỉnh sửa thông tin
            ApplicantEntity existingApplicant = applicantRepository.findById(applicantDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            // thêm lại các thuộc tính không thuộc trường thay đổi thông tin
            // các thuộc tính là thực thể và liên quan
//            companyEntity.setJobPostings(existingCompany.getJobPostings());
//            companyEntity.setRating(existingCompany.getRating());
//            companyEntity.setRateCompanyEntities(existingCompany.getRateCompanyEntities());
//            companyEntity.setFollowCompanyEntities(existingCompany.getFollowCompanyEntities());
//            companyEntity.setFields(existingCompany.getFields());
//            companyEntity.setUsedToWorkEntities(existingCompany.getUsedToWorkEntities());
//            companyEntity.setBlockedUsers(existingCompany.getBlockedUsers());
//            companyEntity.setBlockingUsers(existingCompany.getBlockingUsers());
//            companyEntity.setNotifications(existingCompany.getNotifications());
//            companyEntity.setRateApplicantEntities(existingCompany.getRateApplicantEntities());
            applicantEntity.setApplications(existingApplicant.getApplications());

            // các thuộc tính không phải thực thể

            // xóa hết thuộc tính cũ
            existingApplicant.getPhoneNumbers().clear();
            phoneNumberRepository.deleteAll(existingApplicant.getPhoneNumbers());
            existingApplicant.getEmails().clear();
            emailRepository.deleteAll(existingApplicant.getEmails());
//            existingCompany.getWards().removeAll(existingCompany.getWards());
            existingApplicant.getCertifications().clear();
            certificationRepository.deleteAll(existingApplicant.getCertifications());
            existingApplicant.getSkills().clear();
            skillRepository.deleteAll(existingApplicant.getSkills());
        } else {
            // trường hợp tạo mới
            applicantEntity.setIsBanned(false);
            applicantEntity.setIsActive(true);
            applicantEntity.setIsPublic(true);
        }
        // các thộc tính nằm ở bảng khác
//        companyRepository.save(companyEntity);
        // PhoneNumber
        if(applicantEntity.getPhoneNumbers() != null && !applicantEntity.getPhoneNumbers().isEmpty()) {
            applicantEntity.getPhoneNumbers().clear();
        }
        List<String> phoneNumbers = applicantDTO.getPhoneNumbers();
        if(phoneNumbers != null && !phoneNumbers.isEmpty()) {
            for(String phoneNumber : phoneNumbers) {
                PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
                phoneNumberEntity.setPhoneNumber(phoneNumber);
                phoneNumberEntity.setUser(applicantEntity);
//                phoneNumberRepository.save(phoneNumberEntity);
                applicantEntity.getPhoneNumbers().add(phoneNumberEntity);
            }
        }
        // Email
        if(applicantEntity.getEmails() != null && !applicantEntity.getEmails().isEmpty()) {
            applicantEntity.getEmails().clear();
        }
        List<String> emails = applicantDTO.getEmails();
        if(emails != null && !emails.isEmpty()) {
            for(String email : emails) {
                EmailEntity emailEntity = new EmailEntity();
                emailEntity.setEmail(email);
                emailEntity.setUser(applicantEntity);
//                emailRepository.save(emailEntity);
                applicantEntity.getEmails().add(emailEntity);
            }
        }
        //certìfication
        if(applicantEntity.getCertifications() != null && !applicantEntity.getCertifications().isEmpty()) {
            applicantEntity.getCertifications().clear();
        }
        List<CertificationDTO> certificationDTOS = applicantDTO.getCertifications();
        if(certificationDTOS != null && !certificationDTOS.isEmpty()) {
            for (CertificationDTO certificationDTO : certificationDTOS) {
                CertificationEntity certificationEntity = modelMapper.map(certificationDTO, CertificationEntity.class);
                certificationEntity.setApplicant(applicantEntity);
                applicantEntity.getCertifications().add(certificationEntity);
            }
        }
        // Ward
        applicantEntity.setSpecificAddress(applicantDTO.getSpecificAddress());
        WardEntity wardEntity = wardRepository.findById(applicantDTO.getWard().getId()).get();
        applicantEntity.setWard(wardEntity);

//        // Field
//        List<FieldDTO> fieldDTOs = companyDTO.getFields();
//        if (fieldDTOs != null && !fieldDTOs.isEmpty()) {
//            for (FieldDTO fieldDTO : fieldDTOs) {
//                FieldEntity fieldEntity = fieldRepository.findById(fieldDTO.getId())
//                        .orElseThrow(() -> new RuntimeException("Field not found"));
//                companyEntity.getFields().add(fieldEntity);
//            }
//        }
        List<SkillDTO> skillDTOS = applicantDTO.getSkills();
        if(skillDTOS != null && !skillDTOS.isEmpty()) {
            for(SkillDTO skillDTO : skillDTOS) {
                SkillEntity skillEntity = skillRepository.findById(skillDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Skill not found"));
                applicantEntity.getSkills().add(skillEntity);
            }
        }
        applicantEntity = applicantRepository.save(applicantEntity);
        return  applicantEntity;
    }
    public ApplicantDTO toApplicantDTO(ApplicantEntity applicantEntity) {
        ApplicantDTO applicantDTO = ApplicantDTO.builder()
                .id(applicantEntity.getId())
                .username(applicantEntity.getUsername())
                .password(applicantEntity.getPassword())
                .isActive(applicantEntity.getIsActive())
                .description(applicantEntity.getDescription())
                .isPublic(applicantEntity.getIsPublic())
                .isBanned(applicantEntity.getIsBanned())
                .isAvailable(applicantEntity.getIsAvailable())
                .image(applicantEntity.getImage())
                .specificAddress(applicantEntity.getSpecificAddress())
                .firstName(applicantEntity.getFirstName())
                .lastName(applicantEntity.getLastName())
                .dob(applicantEntity.getDob())
                .build();

        // set address
        if(applicantEntity.getWard() != null) {
            WardEntity wardEntity = applicantEntity.getWard();
            String wardName = wardEntity.getName();
            String cityName = wardEntity.getCity().getName();
            String provinceName = wardEntity.getCity().getProvince().getName();
            String fullAddress = wardName + ", " + cityName + ", " + provinceName;
            if (StringUtils.notEmptyData(applicantEntity.getSpecificAddress())) {
                fullAddress = applicantEntity.getSpecificAddress() + ", " + fullAddress;
            }
            applicantDTO.setFullAddress(fullAddress);
            WardDTO wardDTO = new WardDTO(wardEntity);
            applicantDTO.setWard(wardDTO);
        }

        //jobtype
        if (applicantEntity.getApplicantJobtypeEntities() != null && !applicantEntity.getApplicantJobtypeEntities().isEmpty()) {
            List<JobTypeDTO> jobTypes = applicantEntity.getApplicantJobtypeEntities().stream()
                    .map(applicantJobtypeEntity -> {
                        JobTypeDTO jobTypeDTO = modelMapper.map(applicantJobtypeEntity.getJobType(), JobTypeDTO.class);
                        jobTypeDTO.setLevel(applicantJobtypeEntity.getLevel());
                        return jobTypeDTO;
                    })
                    .collect(Collectors.toList());
            applicantDTO.setJobTypes(jobTypes);
        }

        //skill
        if (applicantEntity.getSkills() != null && !applicantEntity.getSkills().isEmpty()) {
            List<SkillDTO> skills = applicantEntity.getSkills().stream()
                    .map(skillEntity -> {
                        SkillDTO skillDTO = modelMapper.map(skillEntity, SkillDTO.class);
                        return skillDTO;
                    })
                    .collect(Collectors.toList());
            applicantDTO.setSkills(skills);
        }

        //certification
        if (applicantEntity.getCertifications() != null && !applicantEntity.getCertifications().isEmpty()) {
            List<CertificationDTO> certifications = applicantEntity.getCertifications().stream()
                    .map(certificationEntity -> {
                        CertificationDTO certificationDTO = modelMapper.map(certificationEntity, CertificationDTO.class);
                        return certificationDTO;
                    })
                    .collect(Collectors.toList());
            applicantDTO.setCertifications(certifications);
        }


        if (applicantEntity.getPhoneNumbers() != null && !applicantEntity.getPhoneNumbers().isEmpty()) {
            List<String> phoneNumbers = applicantEntity.getPhoneNumbers().stream()
                    .map(PhoneNumberEntity::getPhoneNumber)
                    .collect(Collectors.toList());
            applicantDTO.setPhoneNumbers(phoneNumbers);
        }

        if (applicantEntity.getEmails() != null && !applicantEntity.getEmails().isEmpty()) {
            List<String> emails = applicantEntity.getEmails().stream()
                    .map(EmailEntity::getEmail)
                    .collect(Collectors.toList());
            applicantDTO.setEmails(emails);
        }

        return applicantDTO;
    }

    public ApplicantPublicResponse toApplicantPublicResponse(ApplicantEntity applicantEntity) {
        ApplicantPublicResponse applicantResponse = modelMapper.map(applicantEntity, ApplicantPublicResponse.class);

        if (applicantEntity.getWard() != null) {
            WardEntity wardEntity = applicantEntity.getWard();
            String wardName = wardEntity.getName();
            String cityName = wardEntity.getCity().getName();
            String provinceName = wardEntity.getCity().getProvince().getName();
            String fullAddress = wardName + ", " + cityName + ", " + provinceName;
            if (StringUtils.notEmptyData(applicantEntity.getSpecificAddress())) {
                fullAddress = applicantEntity.getSpecificAddress() + ", " + fullAddress;
            }
            applicantResponse.setFullAddress(fullAddress);
        }

        if (applicantEntity.getPhoneNumbers() != null && !applicantEntity.getPhoneNumbers().isEmpty()) {
            List<String> phoneNumbers = applicantEntity.getPhoneNumbers().stream()
                    .map(PhoneNumberEntity::getPhoneNumber)
                    .collect(Collectors.toList());
            applicantResponse.setPhoneNumbers(phoneNumbers);
        }

        if (applicantEntity.getEmails() != null && !applicantEntity.getEmails().isEmpty()) {
            List<String> emails = applicantEntity.getEmails().stream()
                    .map(EmailEntity::getEmail)
                    .collect(Collectors.toList());
            applicantResponse.setEmails(emails);
        }

        if (applicantEntity.getApplicantJobtypeEntities() != null && !applicantEntity.getApplicantJobtypeEntities().isEmpty()) {
            List<String> jobTypes = applicantEntity.getApplicantJobtypeEntities().stream()
                    .map(applicantJobtypeEntity -> {
                        JobTypeEntity jobTypeEntity = applicantJobtypeEntity.getJobType();
                        return jobTypeEntity.getName();
                    })
                    .collect(Collectors.toList());
            applicantResponse.setJobTypes(jobTypes);
        }

        if (applicantEntity.getSkills() != null && !applicantEntity.getSkills().isEmpty()) {
            List<String> skills = applicantEntity.getSkills().stream()
                    .map(SkillEntity::getName)
                    .collect(Collectors.toList());
            applicantResponse.setSkills(skills);
        }

        if (applicantEntity.getCertifications() != null && !applicantEntity.getCertifications().isEmpty()) {
            List<CertificationDTO> certifications = applicantEntity.getCertifications().stream()
                    .map(certificationEntity -> {
                        CertificationDTO certificationDTO = modelMapper.map(certificationEntity, CertificationDTO.class);
                        return certificationDTO;
                    })
                    .collect(Collectors.toList());
            applicantResponse.setCertifications(certifications);
        }

        return applicantResponse;
    }
}