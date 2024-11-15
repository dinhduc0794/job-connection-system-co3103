package com.javaweb.jobconnectionsystem.repository.custom;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;

import java.util.List;
import java.util.Map;

public interface JobPostingRepositoryCustom {
    List<JobPostingEntity> findAll(JobPostingSearchRequest params);
}
