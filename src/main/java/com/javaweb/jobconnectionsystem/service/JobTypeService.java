package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import com.javaweb.jobconnectionsystem.model.dto.JobTypeDTO;

import java.util.List;
import java.util.Optional;

public interface JobTypeService {
    // Thêm loại công việc
    JobTypeEntity addJobType(JobTypeEntity jobType);

    List<JobTypeEntity> getAllJobTypes();

    // Lấy tất cả loại công việc
    List<JobTypeDTO> getAllJobTypeDTOs();

    // Lấy loại công việc theo ID
    Optional<JobTypeEntity> getJobTypeById(Long id);

    // Cập nhật loại công việc
    JobTypeEntity updateJobType(Long id, JobTypeEntity jobTypeDetails);

    // Xóa loại công việc theo ID
    void deleteJobTypeById(Long id);
}
