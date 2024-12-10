package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.BlockingDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.model.response.UserResponse;
import java.util.List;
public interface BlockingService {
    BlockUserEntity saveBlocking(BlockingDTO blockingDTO);
    List<UserResponse> getAllBlockedUserById( Long userId);
    void deleteBlocked(Long blockedId);
}
