package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.JobPostingConverter;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.repository.JobPostingRepository;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import jakarta.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobPostingServiceImpl implements JobPostingService {
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private JobPostingConverter jobPostingConverter;

    @Override
    public List<JobPostingSearchResponse> getAllJobPostings() {
        List<JobPostingEntity> jobPostingEntities = jobPostingRepository.findAll();

        List<JobPostingSearchResponse> jobPostingResponses = new ArrayList<>();
        for (JobPostingEntity ent : jobPostingEntities) {
            jobPostingResponses.add(jobPostingConverter.toJobPostingSearchResponse(ent));
        }
        return jobPostingResponses;
    }

    @Override
    public List<JobPostingSearchResponse> getAllActiveJobPostings() {
        List<JobPostingEntity> jobPostingEntities = jobPostingRepository.findAllByStatus(true);

        List<JobPostingSearchResponse> jobPostingResponses = new ArrayList<>();
        for (JobPostingEntity ent : jobPostingEntities) {
            jobPostingResponses.add(jobPostingConverter.toJobPostingSearchResponse(ent));
        }
        return jobPostingResponses;
    }

    @Override
    public List<JobPostingSearchResponse> getAllJobPostings(JobPostingSearchRequest params, Pageable pageable) {
        List<JobPostingEntity> jobPostingEntities = jobPostingRepository.findAll(params, pageable);

        List<JobPostingSearchResponse> jobPostingResponses = new ArrayList<>();
        for (JobPostingEntity ent : jobPostingEntities) {
            jobPostingResponses.add(jobPostingConverter.toJobPostingSearchResponse(ent));
        }
        return jobPostingResponses;
    }

    @Override
    public int countTotalItems(JobPostingSearchRequest params) {
        return jobPostingRepository.countTotalItems(params);
    }

    @Override
    public JobPostingDetailResponse getJobPostingById(Long id) {
        JobPostingEntity jobPostingEntity = jobPostingRepository.findById(id).get();
        JobPostingDetailResponse jobPosting = jobPostingConverter.toJobPostingDetailResponse(jobPostingEntity);
        return jobPosting;
    }

    @Override
    public JobPostingEntity saveJobPosting(JobPostingDTO jobPostingDTO) {
        JobPostingEntity jobPostingEntity = jobPostingConverter.toJobPostingEntity(jobPostingDTO);

        return jobPostingRepository.save(jobPostingEntity);
    }


    @Override
    public JobPostingEntity updateJobPosting(Long id, JobPostingEntity jobPostingDetails) {
        JobPostingEntity jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Posting not found"));

        jobPosting.setDescription(jobPostingDetails.getDescription());
        jobPosting.setSchedule(jobPostingDetails.getSchedule());
        jobPosting.setMinSalary(jobPostingDetails.getMinSalary());
        jobPosting.setMaxSalary(jobPostingDetails.getMaxSalary());
        jobPosting.setJobType(jobPostingDetails.getJobType());
        jobPosting.setStatus(jobPostingDetails.getStatus());

        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public void deleteJobPostingById(Long id) {
        JobPostingEntity jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Posting not found"));
        jobPostingRepository.delete(jobPosting);
    }
}
