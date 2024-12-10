package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobTypeDTO;
import com.javaweb.jobconnectionsystem.repository.JobTypeRepository;
import com.javaweb.jobconnectionsystem.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobTypeServiceImpl implements JobTypeService {

    @Autowired
    private JobTypeRepository jobTypeRepository;

    @Override
    public JobTypeEntity addJobType(JobTypeEntity jobType) {
        if (jobType == null) {
            return null;
        }
        return jobTypeRepository.save(jobType);
    }

    @Override
    public List<JobTypeEntity> getAllJobTypes() {
        return jobTypeRepository.findAll();
    }

    @Override
    public List<JobTypeDTO> getAllJobTypeDTOs() {
        List<JobTypeEntity> jobTypeEntities = jobTypeRepository.findAll();
        List<JobTypeDTO> jobTypeDTOS = new ArrayList<>();
        for (JobTypeEntity jobTypeEntity : jobTypeEntities) {
            JobTypeDTO jobTypeDTO = new JobTypeDTO();
            jobTypeDTO.setId(jobTypeEntity.getId());
            jobTypeDTO.setName(jobTypeEntity.getName());
            jobTypeDTOS.add(jobTypeDTO);
        }
        return jobTypeDTOS;
    }

    @Override
    public Optional<JobTypeEntity> getJobTypeById(Long id) {
        return jobTypeRepository.findById(id);
    }

    @Override
    public JobTypeEntity updateJobType(Long id, JobTypeEntity jobTypeDetails) {
        JobTypeEntity jobType = jobTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobType not found"));

        jobType.setName(jobTypeDetails.getName());
        jobType.setField(jobTypeDetails.getField());
        jobType.setSkills(jobTypeDetails.getSkills());

        return jobTypeRepository.save(jobType);
    }

    @Override
    public void deleteJobTypeById(Long id) {
        JobTypeEntity jobType = jobTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobType not found"));
        jobTypeRepository.delete(jobType);
    }
}
