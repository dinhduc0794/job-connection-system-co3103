package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.repository.JobPostingRepository;
import com.javaweb.jobconnectionsystem.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Override
    public List<JobPostingEntity> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    @Override
    public JobPostingEntity addJobPosting(JobPostingEntity jobPosting) {
        if (jobPosting == null) {
            return null;
        }
        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public Optional<JobPostingEntity> getJobPostingById(Long id) {
        Optional<JobPostingEntity> jobPosting = jobPostingRepository.findById(id);
        if (jobPosting.isEmpty()) {
            return Optional.empty();
        }
        return jobPosting;
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
