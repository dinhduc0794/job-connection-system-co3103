package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.ApplicantEntity;
import com.javaweb.jobconnectionsystem.entity.InterestedPostEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.dto.InterestedPostDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.InterestedPostRepository;
import com.javaweb.jobconnectionsystem.repository.JobPostingRepository;
import com.javaweb.jobconnectionsystem.service.InterestedPostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterestedPostServiceImpl implements InterestedPostService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private InterestedPostRepository interestedPostRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Override
    public InterestedPostEntity saveInterestedPost(InterestedPostDTO interestedPostDTO){
        InterestedPostEntity interestedPostEntity = modelMapper.map(interestedPostDTO, InterestedPostEntity.class);

        ApplicantEntity applicantEntity = applicantRepository.findById(interestedPostDTO.getApplicantId()).get();
        JobPostingEntity jobPostingEntity = jobPostingRepository.findById(interestedPostDTO.getJobPostingId()).get();

        if(interestedPostDTO.getId() != null || interestedPostRepository.existsByApplicantAndJobPosting(applicantEntity, jobPostingEntity)) {
            interestedPostRepository.delete(interestedPostRepository.findByApplicantAndJobPosting(applicantEntity, jobPostingEntity));
        }
        if(interestedPostDTO.getApplicantId() != null) {
            interestedPostEntity.setApplicant(applicantEntity);
        }
        if(interestedPostDTO.getJobPostingId() != null) {
            interestedPostEntity.setJobPosting(jobPostingEntity);
        }

        return interestedPostRepository.save(interestedPostEntity);
    }

    @Override
    public void deleteInterestedPost(Long id){
        if (!interestedPostRepository.existsById(id)) {
            throw new RuntimeException("Interested post not found");
        }
        interestedPostRepository.deleteById(id);
    }
}
