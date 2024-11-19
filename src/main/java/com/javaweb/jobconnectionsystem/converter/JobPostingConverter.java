package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobPostingConverter {
    @Autowired
    private ModelMapper modelMapper;

    public JobPostingSearchResponse toJobPostingSearchResponse(JobPostingEntity ent){
        JobPostingSearchResponse jobPosting = modelMapper.map(ent, JobPostingSearchResponse.class);

        // level, schedule, jobType, company, skills, province, city, ward, postDate are not mapped
        if (ent.getLevel() != null) {
            String level = ent.getLevel().getValue();
            jobPosting.setLevel(level);
        }

        if (ent.getSchedule() != null) {
            String schedule = ent.getSchedule().getValue();
            jobPosting.setSchedule(schedule);
        }

        if (ent.getJobType() != null) {
            String jobType = ent.getJobType().getName();
            jobPosting.setJobType(jobType);

            if (ent.getJobType().getSkills() != null) {
                List<SkillEntity> skills = ent.getJobType().getSkills();
                if(skills != null){
                    String strSkills = skills.stream()
                            .map(it->it.getName().toString())
                            .collect(Collectors.joining(", "));
                    jobPosting.setSkills(strSkills);
                }
            }
        }


        if(ent.getWard() != null){
            WardEntity ward = ent.getWard();
            if(ward != null){
                String wardName = ward.getName();
                jobPosting.setWard(wardName);

                String cityName = ward.getCity().getName();
                jobPosting.setCity(cityName);

                String provinceName = ward.getCity().getProvince().getName();
                jobPosting.setProvince(provinceName);
            }
        }

        if (ent.getCompany() != null) {
            String companyName = ent.getCompany().getName();
            jobPosting.setCompanyName(companyName);

            String companyImage = ent.getCompany().getImage();
            jobPosting.setCompanyImage(companyImage);
        }

        return jobPosting;
    }

    public JobPostingEntity toJobPostingEntity(JobPostingDTO jobPostingDTO){
        JobPostingEntity jobPostingEntity = modelMapper.map(jobPostingDTO, JobPostingEntity.class);
        // level, schedule, jobType, company, skills, province, city, ward, postDate are not mapped
        if (jobPostingDTO.getLevel() != null) {
            jobPostingEntity.setLevel(jobPostingDTO.getLevel());
        }

        if (jobPostingDTO.getSchedule() != null) {
            jobPostingEntity.setSchedule(jobPostingDTO.getSchedule());
        }

        if (jobPostingDTO.getJobTypeId() != null) {
            JobTypeEntity jobTypeEntity = new JobTypeEntity();
            jobTypeEntity.setId(jobPostingDTO.getJobTypeId());
            jobPostingEntity.setJobType(jobTypeEntity);
        }

        if (jobPostingDTO.getCompanyId() != null) {
            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setId(jobPostingDTO.getCompanyId());
            jobPostingEntity.setCompany(companyEntity);
        }

        if (jobPostingDTO.getWardId() != null) {
            WardEntity wardEntity = new WardEntity();
            wardEntity.setId(jobPostingDTO.getWardId());
            jobPostingEntity.setWard(wardEntity);
        }

        return jobPostingEntity;
    }

    public JobPostingDetailResponse toJobPostingDetailResponse(JobPostingEntity ent) {
        JobPostingDetailResponse jobPosting = modelMapper.map(ent, JobPostingDetailResponse.class);

        // level, schedule, jobType, company, skills, province, city, ward, postDate are not mapped
        if (ent.getLevel() != null) {
            String level = ent.getLevel().getValue();
            jobPosting.setLevel(level);
        }

        if (ent.getSchedule() != null) {
            String schedule = ent.getSchedule().getValue();
            jobPosting.setSchedule(schedule);
        }

        if (ent.getJobType() != null) {
            String jobType = ent.getJobType().getName();
            jobPosting.setJobType(jobType);

            if (ent.getJobType().getSkills() != null) {
                List<SkillEntity> skills = ent.getJobType().getSkills();
                if(skills != null){
                    String strSkills = skills.stream()
                            .map(it->it.getName().toString())
                            .collect(Collectors.joining(", "));
                    jobPosting.setSkills(strSkills);
                }
            }
        }

        if (ent.getCompany() != null) {
            String companyName = ent.getCompany().getName();
            jobPosting.setCompanyName(companyName);

            String emails = ent.getCompany().getEmails().stream()
                    .map(it->it.getEmail())
                    .collect(Collectors.joining(", "));
            jobPosting.setEmails(emails);

            String phoneNumbers = ent.getCompany().getPhoneNumbers().stream()
                    .map(it->it.getPhoneNumber())
                    .collect(Collectors.joining(", "));
            jobPosting.setPhoneNumbers(phoneNumbers);
        }

        if(ent.getWard() != null){
            WardEntity ward = ent.getWard();
            if(ward != null){
                String wardName = ward.getName();
                jobPosting.setWard(wardName);

                String cityName = ward.getCity().getName();
                jobPosting.setCity(cityName);

                String provinceName = ward.getCity().getProvince().getName();
                jobPosting.setProvince(provinceName);

                jobPosting.setAddress(wardName + ", " + cityName + ", " + provinceName);
            }
        }

        // postDate to GMT+7
        if (ent.getPostDates() != null && !ent.getPostDates().isEmpty()) {
            ZonedDateTime postedDate = ent.getPostDates().get(0).getDatetime();
            ZonedDateTime gmt7PostedDate = postedDate.withZoneSameInstant(ZoneId.of("GMT+7"));
            jobPosting.setPostedDate(gmt7PostedDate);
        }

        if (ent.getCompany() != null) {
            String companyName = ent.getCompany().getName();
            jobPosting.setCompanyName(companyName);

            String companyImage = ent.getCompany().getImage();
            jobPosting.setCompanyImage(companyImage);

            String companyField = ent.getCompany().getFields().stream()
                    .map(it->it.getName())
                    .collect(Collectors.joining(", "));
            jobPosting.setCompanyField(companyField);
        }



        return jobPosting;
    }
}
