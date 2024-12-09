package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;

import com.javaweb.jobconnectionsystem.enums.LevelEnum;
import com.javaweb.jobconnectionsystem.model.dto.*;
import com.javaweb.jobconnectionsystem.model.response.ApplicantApplicationReponse;
import com.javaweb.jobconnectionsystem.model.response.ApplicantPublicResponse;
import com.javaweb.jobconnectionsystem.repository.*;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
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

            if(applicantDTO.getIsPublic() == null) applicantEntity.setIsPublic(existingApplicant.getIsPublic());
            else applicantEntity.setIsPublic(applicantDTO.getIsPublic());

            // xóa hết thuộc tính cũ
            existingApplicant.getPhoneNumbers().clear();
            phoneNumberRepository.deleteAll(existingApplicant.getPhoneNumbers());
            existingApplicant.getEmails().clear();
            emailRepository.deleteAll(existingApplicant.getEmails());
            existingApplicant.getCertifications().clear();
            certificationRepository.deleteAll(existingApplicant.getCertifications());
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
        List<SkillDTO> skills = applicantDTO.getSkills();
        if(skills != null && !skills.isEmpty()) {
            for (SkillDTO skill : skills) {
                SkillEntity skillEntity = skillRepository.findById(skill.getId()).get();
                applicantEntity.getSkills().add(skillEntity);
            }
        }
        return  applicantEntity;
    }

    public ApplicantDTO toApplicantDTO(ApplicantEntity applicantEntity) {
        ApplicantDTO applicantDTO = modelMapper.map(applicantEntity, ApplicantDTO.class);

        // set address
        if (applicantEntity.getWard() != null) {
            WardEntity wardEntity = applicantEntity.getWard();
            String wardName = wardEntity.getName();
            String cityName = wardEntity.getCity().getName();
            String provinceName = wardEntity.getCity().getProvince().getName();
            String fullAddress = wardName + ", " + cityName + ", " + provinceName;
            if (StringUtils.notEmptyData(applicantEntity.getSpecificAddress())) {
                fullAddress = applicantEntity.getSpecificAddress() + ", " + fullAddress;
            }
            applicantDTO.setFullAddress(fullAddress);
            applicantDTO.getWard().setId(wardEntity.getId());
            applicantDTO.getWard().setName(wardEntity.getName());
        }

        //jobtype
        List<ApplicantJobtypeEntity> applicantJobtypeEntities = applicantEntity.getApplicantJobtypeEntities();
        if(applicantJobtypeEntities != null && !applicantJobtypeEntities.isEmpty()) {
            for(ApplicantJobtypeEntity applicantJobtypeEntity : applicantJobtypeEntities) {
                JobTypeEntity jobTypeEntity = applicantJobtypeEntity.getJobType();
                JobTypeDTO jobTypeDTO = new JobTypeDTO();
                jobTypeDTO.setLevel(applicantJobtypeEntity.getLevel());
                jobTypeDTO.setName(jobTypeEntity.getName());
                jobTypeDTO.setId(jobTypeEntity.getId());

                applicantDTO.getJobTypes().add(jobTypeDTO);
            }
        }

        //skill
        List<SkillEntity> skillEntities = applicantEntity.getSkills();
        if(skillEntities != null && !skillEntities.isEmpty()) {
            for(SkillEntity skillEntity : skillEntities) {
                SkillDTO skillDTO = new SkillDTO();
                skillDTO.setId(skillEntity.getId());
                skillDTO.setName(skillEntity.getName());

                applicantDTO.getSkills().add(skillDTO);
            }
        }
        //certification
        List<CertificationEntity> certificationEntities = applicantEntity.getCertifications();
        if(certificationEntities != null && !certificationEntities.isEmpty()) {
            for(CertificationEntity certificationEntity : certificationEntities) {
                CertificationDTO certificationDTO = modelMapper.map(certificationEntity, CertificationDTO.class);
                applicantDTO.getCertifications().add(certificationDTO);
            }
        }

        List<PhoneNumberEntity> phoneNumberEntities = applicantEntity.getPhoneNumbers();
        if(phoneNumberEntities != null && !phoneNumberEntities.isEmpty()) {
            for(PhoneNumberEntity phoneNumberEntity : phoneNumberEntities) {
                applicantDTO.getPhoneNumbers().add(phoneNumberEntity.getPhoneNumber());
            }
        }

        List<EmailEntity> emailEntities = applicantEntity.getEmails();
        if(emailEntities != null && !emailEntities.isEmpty()) {
            for(EmailEntity emailEntity : emailEntities) {
                applicantDTO.getEmails().add(emailEntity.getEmail());
            }
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