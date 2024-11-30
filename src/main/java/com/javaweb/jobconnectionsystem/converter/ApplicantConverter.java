package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;

import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.model.response.ApplicanApplicationReponse;
import com.javaweb.jobconnectionsystem.model.response.ApplicantResponse;
import com.javaweb.jobconnectionsystem.model.response.LoginResponse;
import com.javaweb.jobconnectionsystem.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private SkillRepository skillRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobPostingRepository postingRepository;
    @Autowired
    private EmailRepository emailRepository;

    public ApplicantEntity toApplicantEntity(ApplicantDTO applicantDTO) {
        ApplicantEntity applicantEntity = modelMapper.map(applicantDTO, ApplicantEntity.class);
        List<Long> wardIds = applicantDTO.getWardIds();
        if (wardIds != null && !wardIds.isEmpty()) {
            for (Long id : wardIds) {
                WardEntity wardEntity = wardRepository.findById(id).get();
                // Ví dụ thêm WardEntity vào ApplicantEntity (giả sử applicantEntity đã được khởi tạo)
                applicantEntity.getWards().add(wardEntity); // Cần phương thức `addWard` trong `ApplicantEntity`
            }
        }
        List<String> phoneNumbers = applicantDTO.getPhoneNumbers();
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            applicantEntity.setPhoneNumbers(
                    applicantEntity.getPhoneNumbers().stream()
                            .filter(p -> p.getPhoneNumber() != null)
                            .collect(Collectors.toList())
            );

            for (String phoneNumber : phoneNumbers) {
                PhoneNumberEntity newPhoneNumber = new PhoneNumberEntity();
                newPhoneNumber.setPhoneNumber(phoneNumber);
                newPhoneNumber.setUser(applicantEntity);
                applicantEntity.getPhoneNumbers().add(newPhoneNumber);
            }
        }

        List<String> emails = applicantDTO.getEmails();
        if (emails != null && !emails.isEmpty()) {
            applicantEntity.setEmails(
                    applicantEntity.getEmails().stream()
                            .filter(e -> e.getEmail() != null)
                            .collect(Collectors.toList())
            );
            for (String email : emails) {
                EmailEntity newEmail = new EmailEntity();
                newEmail.setEmail(email);
                newEmail.setUser(applicantEntity);
                applicantEntity.getEmails().add(newEmail);
            }
        }
        List<Long> notificationIds = applicantDTO.getNotificationIds();
        if (notificationIds != null && !notificationIds.isEmpty()) {
            for (Long id : notificationIds) {
                NotificationEntity notificationEntity = notificationRepository.findById(id).get();
                if (notificationEntity != null) {
                    applicantEntity.getNotifications().add(notificationEntity);
                }
            }
        }
        List<Long> blockedUserIds = applicantDTO.getBlockedUserIds();
        if (blockedUserIds != null && !blockedUserIds.isEmpty()) {
            for (Long id : blockedUserIds) {
                BlockUserEntity blockedUserEntity = blockUserRepository.findById(id).get();
                if (blockedUserEntity != null) {
                    applicantEntity.getBlockedUsers().add(blockedUserEntity);
                }
            }
        }

        List<Long> skillIds = applicantDTO.getSkillIds();
        if (skillIds != null && !skillIds.isEmpty()) {
            for (Long id : skillIds) {
                SkillEntity skillEntity = skillRepository.findById(id).get();
                if (skillEntity != null) {
                    applicantEntity.getSkills().add(skillEntity);
                }
            }
        }

        return applicantEntity;
    }

    public ApplicantResponse toApplicantResponse(ApplicantEntity applicantEntity) {
        ApplicantResponse applicantResponse = modelMapper.map(applicantEntity, ApplicantResponse.class);
        if (applicantEntity.getWards() != null && !applicantEntity.getWards().isEmpty()) {
            List<Long> wardIds = applicantEntity.getWards().stream()
                    .map(WardEntity::getId)
                    .collect(Collectors.toList());
            applicantResponse.setWardIds(wardIds);
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