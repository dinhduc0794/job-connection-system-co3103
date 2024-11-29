package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.model.dto.InterestedPostDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;

public interface InterestedPostService {
    ResponseDTO saveInterestedPost(InterestedPostDTO interestedPostDTO);
}
