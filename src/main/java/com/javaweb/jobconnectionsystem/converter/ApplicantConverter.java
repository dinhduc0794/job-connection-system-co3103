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

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
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
            ApplicantEntity existingApplicant = applicantRepository.findById(applicantDTO.getId()).get();
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

            if(applicantDTO.getIsPublic() == null) applicantEntity.setIsPublic(existingApplicant.getIsPublic());
            else applicantEntity.setIsPublic(applicantDTO.getIsPublic());
            // xóa phone
            List<PhoneNumberEntity> phoneNumberEntities = existingApplicant.getPhoneNumbers();
            if (phoneNumberEntities != null && !phoneNumberEntities.isEmpty()) {
                for (PhoneNumberEntity phoneNumber : phoneNumberEntities) {
                    phoneNumberRepository.delete(phoneNumber);
                }
                existingApplicant.getPhoneNumbers().clear();
            }
            entityManager.flush();
            // Xóa các emails
            List<EmailEntity> emailEntities = existingApplicant.getEmails();
            if (emailEntities != null && !emailEntities.isEmpty()) {
                for (EmailEntity email : emailEntities) {
                    emailRepository.delete(email);
                }
                existingApplicant.getEmails().clear();
            }
            entityManager.flush();
            // xóa certification
            List<CertificationEntity> certifications = existingApplicant.getCertifications();
            if (certifications != null && !certifications.isEmpty()) {
                for (CertificationEntity certification : certifications) {
                    certificationRepository.delete(certification);
                }
                existingApplicant.getCertifications().clear();
            }
            entityManager.flush();
            List<ApplicantJobtypeEntity> applicantJobtypeEntitys = existingApplicant.getApplicantJobtypeEntities();
            if(applicantJobtypeEntitys != null && !applicantJobtypeEntitys.isEmpty()) {
                for(ApplicantJobtypeEntity applicantJobtypeEntity : applicantJobtypeEntitys) {
                    JobTypeEntity jobTypeEntity = applicantJobtypeEntity.getJobType();
                    jobTypeEntity.getApplicantJobtypeEntities().remove(applicantJobtypeEntity);
                }
            }
            entityManager.flush();
            if(!existingApplicant.getSkills().isEmpty()) {
                List<SkillEntity> skills = skillRepository.findByApplicants_Id(existingApplicant.getId());
                if(skills != null && !skills.isEmpty()) {
                    for (SkillEntity skill : skills) {
                        existingApplicant.getSkills().remove(skill);
                    }
                }
            }
            entityManager.flush();
            applicantRepository.save(existingApplicant);
            entityManager.flush();
        }
        else {
            applicantEntity.setIsBanned(false);
            applicantEntity.setIsActive(true);
            applicantEntity.setIsPublic(true);
        }
        applicantEntity.getPhoneNumbers().clear();
        applicantEntity.getEmails().clear();
        List<String> phoneNumbers = applicantDTO.getPhoneNumbers();
        if(phoneNumbers != null && !phoneNumbers.isEmpty()) {
            for(String phoneNumber : phoneNumbers) {
                PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
                phoneNumberEntity.setPhoneNumber(phoneNumber);
                phoneNumberEntity.setUser(applicantEntity);
                applicantEntity.getPhoneNumbers().add(phoneNumberEntity);
                phoneNumberRepository.save(phoneNumberEntity);
            }
        }
        List<String> emails = applicantDTO.getEmails();
        if(emails != null && !emails.isEmpty()) {
            for(String email : emails) {
                EmailEntity emailEntity = new EmailEntity();
                emailEntity.setEmail(email);
                emailEntity.setUser(applicantEntity);
                applicantEntity.getEmails().add(emailEntity);
                emailRepository.save(emailEntity);
            }
        }
        // Certification
        if(applicantEntity.getCertifications() != null && !applicantEntity.getCertifications().isEmpty()) {
            applicantEntity.getApplications().clear();
        }
        List<CertificationDTO> certificationDTOS = applicantDTO.getCertifications();
        if(certificationDTOS != null && !certificationDTOS.isEmpty()) {
            for(CertificationDTO certificationDTO : certificationDTOS) {
                CertificationEntity certificationEntity = modelMapper.map(certificationDTO, CertificationEntity.class);
                certificationEntity.setApplicant(applicantEntity);
                applicantEntity.getCertifications().add(certificationEntity);
                certificationRepository.save(certificationEntity);
            }
        }
        // Ward
        applicantEntity.setSpecificAddress(applicantDTO.getSpecificAddress());
        WardEntity wardEntity = wardRepository.findById(applicantDTO.getWard().getId()).get();
        applicantEntity.setWard(wardEntity);
        // JobType
        List<JobTypeDTO> jobTypes = applicantDTO.getJobTypes();
        if(jobTypes != null && !jobTypes.isEmpty()) {
            for (JobTypeDTO jobType : jobTypes) {
                LevelEnum level = jobType.getLevel();
                Long jobTypeId = jobType.getId();
                JobTypeEntity jobTypeEntity = jobTypeRepository.findById(jobTypeId).get();
                ApplicantJobtypeEntity applicantJobtypeEntity = new ApplicantJobtypeEntity();
                applicantJobtypeEntity.setJobType(jobTypeEntity);
                applicantJobtypeEntity.setLevel(level);
                applicantJobtypeEntity.setApplicant(applicantEntity);
                applicantJobTypeRepository.save(applicantJobtypeEntity);
                applicantEntity.getApplicantJobtypeEntities().add(applicantJobtypeEntity);
                jobTypeEntity.getApplicantJobtypeEntities().add(applicantJobtypeEntity);
            }
        }
        // Skill
        List<SkillDTO> skills = applicantDTO.getSkills();
        if(skills != null && !skills.isEmpty()) {
            for (SkillDTO skill : skills) {
                SkillEntity skillEntity = skillRepository.findById(skill.getId()).get();
                applicantEntity.getSkills().add(skillEntity);
            }
        }
        applicantRepository.save(applicantEntity);
        return applicantEntity;
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