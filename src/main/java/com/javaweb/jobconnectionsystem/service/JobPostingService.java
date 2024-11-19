package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobPostingService {
    // Lấy tất cả bài đăng công việc
    public List<JobPostingSearchResponse> getAllJobPostings(JobPostingSearchRequest params);

    // Thêm bài đăng công việc
    public JobPostingEntity saveJobPosting(JobPostingDTO jobPostingDTO);


    // Lấy bài đăng công việc theo ID
    public JobPostingDetailResponse getJobPostingById(Long id);

    // Cập nhật bài đăng công việc
    public JobPostingEntity updateJobPosting(Long id, JobPostingEntity jobPosting);

    // Xóa bài đăng công việc theo ID
    public void deleteJobPostingById(Long id);
}
