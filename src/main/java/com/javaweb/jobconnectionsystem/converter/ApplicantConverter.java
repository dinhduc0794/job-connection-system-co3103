package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.model.dto.AddressDTO;
import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.dto.CertificationDTO;
import com.javaweb.jobconnectionsystem.model.dto.JobTypeDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicanApplicationReponse;
import com.javaweb.jobconnectionsystem.model.response.ApplicantResponse;
import com.javaweb.jobconnectionsystem.model.response.LoginResponse;
import com.javaweb.jobconnectionsystem.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public ApplicantEntity toApplicantEntity(ApplicantDTO applicantDTO) {
        // Bước kiểm tra tính hợp lệ của dữ liệu
        if(applicantDTO.getPhoneNumbers() != null && !applicantDTO.getPhoneNumbers().isEmpty()) {
            for(String phoneNumber : applicantDTO.getPhoneNumbers()) {
                if(phoneNumberRepository.existsByPhoneNumber(phoneNumber)) {
                    if(!phoneNumberRepository.findByPhoneNumber(phoneNumber).getUser().getId().equals(applicantDTO.getId())) {
                        throw new RuntimeException("Phonenumber " + phoneNumber + " already exists");
                    }
                }
            }
        }
        if(applicantDTO.getEmails() != null && !applicantDTO.getEmails().isEmpty()) {
            for(String email : applicantDTO.getEmails()) {
                if(emailRepository.existsByEmail(email)) {
                    if(emailRepository.findByEmail(email).getUser().getId() != applicantDTO.getId()) {
                        throw new RuntimeException("Email " + email + " already exists");
                    }
                }
            }
        }
        if(applicantDTO.getUsername() != null) {
            ApplicantEntity ApplicantFromUserName = applicantRepository.findByUsername(applicantDTO.getUsername());
            if (ApplicantFromUserName != null) {
                if(ApplicantFromUserName.getId() != applicantDTO.getId()) {
                    throw new RuntimeException("Username already exists");
                }
            }
        }

        // Sau khi kiểm tra tính hợp lệ của dữ liệu, thực hiện việc chỉnh sửa hoặc tạp mới
        ApplicantEntity applicantEntity = modelMapper.map(applicantDTO, ApplicantEntity.class);

        if (applicantDTO.getId() != null) {
            // trường hợp chỉnh sửa thông tin
            ApplicantEntity existingApplicant = applicantRepository.findById(applicantDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Applicant not found"));
            // thêm lại các thuộc tính không thuộc trường thay đổi thông tin
            // các thuộc tính là thực thể và liên quan
            applicantEntity.setApplications(existingApplicant.getApplications());
            applicantEntity.setRateApplicantEntities(existingApplicant.getRateApplicantEntities());
            applicantEntity.setInterestedPosts(existingApplicant.getInterestedPosts());
            applicantEntity.setIsAvailable(existingApplicant.getIsAvailable());
            applicantEntity.setRateCompanyEntities(existingApplicant.getRateCompanyEntities());
            applicantEntity.setUsedToWorkEntities(existingApplicant.getUsedToWorkEntities());
            applicantEntity.setBlockedUsers(existingApplicant.getBlockedUsers());
            applicantEntity.setBlockingUsers(existingApplicant.getBlockingUsers());
            applicantEntity.setNotifications(existingApplicant.getNotifications());
            applicantEntity.setCertifications(existingApplicant.getCertifications());
            ;
            // các thuộc tính không phải thực
            applicantEntity.setCreatedAt(existingApplicant.getCreatedAt());
            applicantEntity.setIsActive(existingApplicant.getIsActive());
            applicantEntity.setIsBanned(existingApplicant.getIsBanned());
            if(applicantDTO.getIsPublic() == null) applicantEntity.setIsPublic(existingApplicant.getIsPublic());
            else applicantEntity.setIsPublic(applicantDTO.getIsPublic());

            // xóa hết thuộc tính cũ
            existingApplicant.getPhoneNumbers().clear();
            phoneNumberRepository.deleteAll(existingApplicant.getPhoneNumbers());
            existingApplicant.getEmails().clear();
            emailRepository.deleteAll(existingApplicant.getEmails());
//            existingApplicant.getWards().removeAll(existingApplicant.getWards());
            List<ApplicantJobtypeEntity> applicantJobtypeEntitys = existingApplicant.getApplicantJobtypeEntities();
            if(applicantJobtypeEntitys != null && !applicantJobtypeEntitys.isEmpty()) {
                for(ApplicantJobtypeEntity applicantJobtypeEntity : applicantJobtypeEntitys) {
                    JobTypeEntity jobTypeEntity = applicantJobtypeEntity.getJobType();
                    jobTypeEntity.getApplicantJobtypeEntities().remove(applicantJobtypeEntity);
                }
            }
            existingApplicant.getSkills().removeAll(existingApplicant.getSkills());
        } else {
            applicantEntity.setIsBanned(false);
            applicantEntity.setIsActive(true);
            applicantEntity.setIsPublic(true);
        }
        // các thộc tính nằm ở bảng khác
        applicantRepository.save(applicantEntity);
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
                phoneNumberRepository.save(phoneNumberEntity);
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
                emailRepository.save(emailEntity);
                applicantEntity.getEmails().add(emailEntity);
            }
        }
        // Ward
        List<AddressDTO> addressWardIds = applicantDTO.getAddressWardIds();
        if (addressWardIds != null && !addressWardIds.isEmpty()) {
            for (AddressDTO addressWardId : addressWardIds) {
                String address = addressWardId.getAddress();
                Long wardId = addressWardId.getWardId();
                WardEntity wardEntity = wardRepository.findById(wardId).get();
//                applicantEntity.getWards().add(wardEntity);
                applicantEntity.setAddress(address);
            }
        }
        // JobType
        List<JobTypeDTO> levelJobTypeIds = applicantDTO.getLevelJobTypeIds();
        if(levelJobTypeIds != null && !levelJobTypeIds.isEmpty()) {
            for (JobTypeDTO levelJobTypeId : levelJobTypeIds) {
                LevelEnum level = levelJobTypeId.getLevel();
                Long jobTypeId = levelJobTypeId.getJobTypeId();
                JobTypeEntity jobTypeEntity = jobTypeRepository.findById(jobTypeId).get();

                ApplicantJobtypeEntity applicantJobtypeEntity = new ApplicantJobtypeEntity();
                applicantJobtypeEntity.setJobType(jobTypeEntity);
                applicantJobtypeEntity.setLevel(level);
                applicantJobtypeEntity.setApplicant(applicantEntity);
//                applicantJobTypeRepository.save(applicantJobtypeEntity);
                applicantEntity.getApplicantJobtypeEntities().add(applicantJobtypeEntity);
//                jobTypeEntity.getApplicantJobtypeEntities().add(applicantJobtypeEntity);
            }
        }
        // Skill
        List<Long> skillIds = applicantDTO.getSkillIds();
        if(skillIds != null && !skillIds.isEmpty()) {
            for (Long skillId : skillIds) {
                SkillEntity skillEntity = skillRepository.findById(skillId).get();
                applicantEntity.getSkills().add(skillEntity);
            }
        }
        return  applicantEntity;
    }

    public ApplicantResponse toApplicantResponse(ApplicantEntity applicantEntity) {
        ApplicantResponse applicantResponse = modelMapper.map(applicantEntity, ApplicantResponse.class);
        if (applicantEntity.getUserWards() != null && !applicantEntity.getUserWards().isEmpty()) {
            List<String> addressList = new ArrayList<>();
            for (UserWardEntity userWard : applicantEntity.getUserWards()) {
                WardEntity wardEntity = userWard.getWard();
                String wardName = wardEntity.getName();

                String cityName = wardEntity.getCity().getName();

                String provinceName = wardEntity.getCity().getProvince().getName();

                String address = userWard.getAddress() + ", " + wardName + ", " + cityName + ", " + provinceName;
                addressList.add(address);
            }
            applicantResponse.setAddresses(addressList);
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

        if (applicantEntity.getNotifications() != null && !applicantEntity.getNotifications().isEmpty()) {
            List<Long> notificationIds = applicantEntity.getNotifications().stream()
                    .map(NotificationEntity::getId)
                    .collect(Collectors.toList());
            applicantResponse.setNotificationIds(notificationIds);
        }

        if (applicantEntity.getBlockedUsers() != null && !applicantEntity.getBlockedUsers().isEmpty()) {
            List<Long> blockedUserIds = applicantEntity.getBlockedUsers().stream()
                    .map(BlockUserEntity::getId)
                    .collect(Collectors.toList());
            applicantResponse.setBlockedUserIds(blockedUserIds);
        }

        if (applicantEntity.getSkills() != null && !applicantEntity.getSkills().isEmpty()) {
            List<Long> skillIds = applicantEntity.getSkills().stream()
                    .map(SkillEntity::getId)
                    .collect(Collectors.toList());
            applicantResponse.setSkillIds(skillIds);
        }

        if (applicantEntity.getCertifications() != null && !applicantEntity.getCertifications().isEmpty()) {
            List<Long> certificationIds = applicantEntity.getCertifications().stream()
                    .map(CertificationEntity::getId)
                    .collect(Collectors.toList());
            applicantResponse.setCertificationIds(certificationIds);
        }

        return applicantResponse;
    }

    public  ApplicanApplicationReponse convertToEntity(ApplicationEntity  applicationDTO) {
            if (applicationDTO == null) {
                return null;
            }

            ApplicanApplicationReponse dto = new ApplicanApplicationReponse();
            dto.setId(applicationDTO.getId());
            dto.setStatus(applicationDTO.getStatus());
            dto.setEmail(applicationDTO.getEmail());
            dto.setPhoneNumber(applicationDTO.getPhoneNumber());
            dto.setDescription(applicationDTO.getDescription());
            dto.setResume(applicationDTO.getResume());
            dto.setTitle(applicationDTO.getJobPosting().getTitle());
            // Set the jobPostingId
            dto.setJobPostingId(applicationDTO.getJobPosting() != null ? applicationDTO.getJobPosting().getId() : null);

            return dto;
        }


}