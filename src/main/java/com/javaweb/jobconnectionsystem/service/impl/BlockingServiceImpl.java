package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.BlockingDTO;
import com.javaweb.jobconnectionsystem.repository.BlockUserRepository;
import com.javaweb.jobconnectionsystem.repository.UserRepository;
import com.javaweb.jobconnectionsystem.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlockingServiceImpl implements BlockingService {
    @Autowired
    BlockUserRepository blockUserRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public  BlockUserEntity saveBlocking(BlockingDTO blockingDTO){
        BlockUserEntity blockUserEntity = new BlockUserEntity();
        blockUserEntity.setId(blockingDTO.getId());
        if(!userRepository.existsById(blockingDTO.getBlockerId())){
            throw new RuntimeException("User Block Not Found");
        }
        if(!userRepository.existsById(blockingDTO.getBlockedUserID())){
            throw new RuntimeException("User was Blocked Not Found");
        }
        blockUserEntity.setBlocker(userRepository .getOne(blockingDTO.getBlockerId()));
        blockUserEntity.setBlockedUser(userRepository.getOne(blockingDTO.getBlockedUserID()));
        blockUserEntity.setCreatedAt(LocalDateTime.now());
        blockUserRepository.save(blockUserEntity);
        return blockUserEntity;
    }

}
