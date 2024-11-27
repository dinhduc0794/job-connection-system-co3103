package com.javaweb.jobconnectionsystem.repository.custom;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface JobPostingRepositoryCustom {
    List<JobPostingEntity> findAll(JobPostingSearchRequest params, Pageable pageable);
    int countTotalItems(JobPostingSearchRequest jobPostingSearchRequest);
}
