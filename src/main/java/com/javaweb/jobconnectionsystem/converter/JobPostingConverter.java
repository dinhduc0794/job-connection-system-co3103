package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import com.javaweb.jobconnectionsystem.entity.WardEntity;
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
        }

        if (ent.getCompany() != null) {
            String companyName = ent.getCompany().getName();
            jobPosting.setCompanyName(companyName);
        }

        List<SkillEntity> skills = ent.getJobType().getSkills();
        if(skills != null){
            String strSkills = skills.stream()
                    .map(it->it.getName().toString())
                    .collect(Collectors.joining(", "));
            jobPosting.setSkills(strSkills);
        }


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

        // postDate to GMT+7
        ZonedDateTime postedDate = ent.getPostDates().get(0).getDatetime();
        ZonedDateTime gmt7PostedDate = postedDate.withZoneSameInstant(ZoneId.of("GMT+7"));
        jobPosting.setPostedDate(gmt7PostedDate);

        // emails, phoneNumbers are not mapped
        String emails = ent.getCompany().getEmails().stream()
                .map(it->it.getEmail())
                .collect(Collectors.joining(", "));
        jobPosting.setEmails(emails);

        String phoneNumbers = ent.getCompany().getPhoneNumbers().stream()
                .map(it->it.getPhoneNumber())
                .collect(Collectors.joining(", "));
        jobPosting.setPhoneNumbers(phoneNumbers);

        return jobPosting;
    }
}
