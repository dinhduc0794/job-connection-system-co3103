package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobPostingService {
    // Thêm bài đăng công việc
    public JobPostingEntity addJobPosting(JobPostingEntity jobPosting);

    // Lấy tất cả bài đăng công việc
    public List<JobPostingEntity> getAllJobPostings(JobPostingSearchRequest params);

    // Lấy bài đăng công việc theo ID
    public Optional<JobPostingEntity> getJobPostingById(Long id);

    // Cập nhật bài đăng công việc
    public JobPostingEntity updateJobPosting(Long id, JobPostingEntity jobPosting);

    // Xóa bài đăng công việc theo ID
    public void deleteJobPostingById(Long id);
}
