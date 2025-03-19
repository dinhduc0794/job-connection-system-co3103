package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.model.dto.InterestedPostDTO;
import com.javaweb.jobconnectionsystem.model.response.InterestedPostsResponse;
import com.javaweb.jobconnectionsystem.model.response.JobPostingSearchResponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;

import java.util.List;

public interface InterestedPostService {
    ResponseDTO saveInterestedPost(InterestedPostDTO interestedPostDTO);
    List<JobPostingSearchResponse> getInterestedPostsByApplicantId(Long id);
}
