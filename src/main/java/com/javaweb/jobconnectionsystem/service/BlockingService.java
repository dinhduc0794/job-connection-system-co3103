package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.BlockingDTO;

public interface BlockingService {
    BlockUserEntity saveBlocking(BlockingDTO blockingDTO);
}
