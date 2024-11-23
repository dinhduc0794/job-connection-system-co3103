package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;

import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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
    private PhoneNumberRepository phoneNumberRepository;
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
////
        List<String> phoneNumbers = applicantDTO.getPhoneNumbers();
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            applicantEntity.setPhoneNumbers(
                    applicantEntity.getPhoneNumbers().stream()
                            .filter(p -> p.getPhoneNumber() != null)
                            .collect(Collectors.toList())
            );

            for (String phoneNumber : phoneNumbers) {
//                    boolean alreadyExistsInEntity = applicantEntity.getPhoneNumbers().stream()
//
//
//                            .anyMatch(p -> p.getPhoneNumber().equals(phoneNumber));
//                    boolean alreadyExistsInDatabase = phoneNumberRepository.existsByPhoneNumber(phoneNumber);
//
//                    if (!alreadyExistsInEntity && !alreadyExistsInDatabase) {
                PhoneNumberEntity newPhoneNumber = new PhoneNumberEntity();
                newPhoneNumber.setPhoneNumber(phoneNumber);
                newPhoneNumber.setUser(applicantEntity);
                applicantEntity.getPhoneNumbers().add(newPhoneNumber);
//                    }
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
//                    boolean alreadyExistsInEntity = applicantEntity.getEmails().stream()
//                            .anyMatch(e -> e.getEmail().equals(email));
//                    boolean alreadyExistsInDatabase = emailRepository.existsByEmail(email);
//
//                    if (!alreadyExistsInEntity && !alreadyExistsInDatabase) {
                EmailEntity newEmail = new EmailEntity();
                newEmail.setEmail(email);
                newEmail.setUser(applicantEntity);
                applicantEntity.getEmails().add(newEmail);
//                    }
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

        return applicantEntity;

    }
}