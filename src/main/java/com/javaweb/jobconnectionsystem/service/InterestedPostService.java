package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.InterestedPostEntity;
import com.javaweb.jobconnectionsystem.model.dto.InterestedPostDTO;

public interface InterestedPostService {
    public InterestedPostEntity saveInterestedPost(InterestedPostDTO interestedPostDTO);
    public void deleteInterestedPost(Long id);
}
