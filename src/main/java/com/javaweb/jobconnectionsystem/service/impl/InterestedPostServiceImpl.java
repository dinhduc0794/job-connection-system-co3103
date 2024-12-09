package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.InterestedPostDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.JobPostingRepository;
import com.javaweb.jobconnectionsystem.service.InterestedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestedPostServiceImpl implements InterestedPostService {
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Override
    public ResponseDTO saveInterestedPost(InterestedPostDTO interestedPostDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        // Lấy thực thể applicant
        ApplicantEntity applicantEntity = applicantRepository.findById(interestedPostDTO.getApplicantId())
                .orElseThrow(() -> new IllegalArgumentException("Applicant not found"));

        // Lấy thực thể jobPosting
        JobPostingEntity jobPostingEntity = jobPostingRepository.findById(interestedPostDTO.getJobPostingId())
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found"));

        // Kiểm tra xem jobPosting đã tồn tại trong danh sách interestedPosts chưa
        if (applicantEntity.getInterestedPosts().contains(jobPostingEntity)) {
            // Nếu tồn tại, xóa mối quan hệ
            applicantEntity.getInterestedPosts().remove(jobPostingEntity);
            responseDTO.setMessage("Uninterested post successfully");
        } else {
            // Nếu chưa tồn tại, thêm mối quan hệ
            applicantEntity.getInterestedPosts().add(jobPostingEntity);
            responseDTO.setMessage("Save interested post successfully");
        }

        // Lưu lại applicantEntity
        applicantRepository.save(applicantEntity);

        return responseDTO;
    }
}
