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

            if(applicantDTO.getIsPublic() == null) applicantEntity.setIsPublic(existingApplicant.getIsPublic());
            else applicantEntity.setIsPublic(applicantDTO.getIsPublic());

            // xóa hết thuộc tính cũ
            existingApplicant.getCertifications().clear();
            certificationRepository.deleteAll(existingApplicant.getCertifications());
            List<ApplicantJobtypeEntity> applicantJobtypeEntitys = existingApplicant.getApplicantJobtypeEntities();
            if(applicantJobtypeEntitys != null && !applicantJobtypeEntitys.isEmpty()) {
                for(ApplicantJobtypeEntity applicantJobtypeEntity : applicantJobtypeEntitys) {
                    JobTypeEntity jobTypeEntity = applicantJobtypeEntity.getJobType();
                    jobTypeEntity.getApplicantJobtypeEntities().remove(applicantJobtypeEntity);
                    applicantEntity.getApplicantJobtypeEntities().remove(applicantJobtypeEntity);
                    applicantJobTypeRepository.delete(applicantJobtypeEntity);
                }
            }
            existingApplicant.getSkills().removeAll(existingApplicant.getSkills());
        } else {
            applicantEntity.setIsBanned(false);
            applicantEntity.setIsActive(true);
            applicantEntity.setIsPublic(true);
            applicantEntity.setIsAvailable(true);
        }
        // các thộc tính nằm ở bảng khác
        //     applicantRepository.save(applicantEntity);
        // PhoneNumber
        applicantEntity.getPhoneNumbers().clear();
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
        applicantEntity.getEmails().clear();
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
        // Certification
        if(applicantEntity.getCertifications() != null && !applicantEntity.getCertifications().isEmpty()) {
            applicantEntity.getApplications().clear();
        }
        List<CertificationDTO> certificationDTOS = applicantDTO.getCertifications();
        if(certificationDTOS != null && !certificationDTOS.isEmpty()) {
            for(CertificationDTO certificationDTO : certificationDTOS) {
                CertificationEntity certificationEntity = modelMapper.map(certificationDTO, CertificationEntity.class);
                certificationEntity.setApplicant(applicantEntity);
                certificationRepository.save(certificationEntity);
                applicantEntity.getCertifications().add(certificationEntity);
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
        applicantEntity.getSkills().clear();
        List<SkillDTO> skills = applicantDTO.getSkills();
        if(skills != null && !skills.isEmpty()) {
            for (SkillDTO skill : skills) {
                SkillEntity skillEntity = skillRepository.findById(skill.getId()).get();
                skillEntity.getApplicants().add(applicantEntity);
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