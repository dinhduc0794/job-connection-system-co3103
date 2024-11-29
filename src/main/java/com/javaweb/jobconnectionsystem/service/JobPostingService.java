package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobPostingDTO;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.model.response.JobPostingDetailResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobPostingService {
    List<JobPostingSearchResponse> getAllJobPostings();

    List<JobPostingSearchResponse> getAllActiveJobPostings();
    // Lấy tất cả bài đăng công việc
    List<JobPostingSearchResponse> getAllJobPostings(JobPostingSearchRequest params, Pageable pageable);

    int countTotalItems(JobPostingSearchRequest params);

    // Thêm bài đăng công việc
    JobPostingEntity saveJobPosting(JobPostingDTO jobPostingDTO);


    // Lấy bài đăng công việc theo ID
    JobPostingDetailResponse getJobPostingById(Long id);

    // Cập nhật bài đăng công việc
    JobPostingEntity updateJobPosting(Long id, JobPostingEntity jobPosting);

    // Xóa bài đăng công việc theo ID
    void deleteJobPostingById(Long id);
}
