package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.UserConverter;
import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.BlockingDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.model.response.UserResponse;
import com.javaweb.jobconnectionsystem.repository.BlockUserRepository;
import com.javaweb.jobconnectionsystem.repository.UserRepository;
import com.javaweb.jobconnectionsystem.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockingServiceImpl implements BlockingService {
    @Autowired
    BlockUserRepository blockUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserConverter userConverter;
    @Override
    public  BlockUserEntity saveBlocking(BlockingDTO blockingDTO){
        BlockUserEntity blockUserEntity = new BlockUserEntity();
        blockUserEntity.setId(blockingDTO.getId());
        if(!userRepository.existsById(blockingDTO.getBlockerId())){
            throw new RuntimeException("User Block Not Found");
        }
        if(!userRepository.existsById(blockingDTO.getBlockedUserId())){
            throw new RuntimeException("User was Blocked Not Found");
        }
        blockUserEntity.setBlocker(userRepository .getOne(blockingDTO.getBlockerId()));
        blockUserEntity.setBlockedUser(userRepository.getOne(blockingDTO.getBlockedUserId()));
        blockUserEntity.setCreatedAt(LocalDateTime.now());
        blockUserRepository.save(blockUserEntity);
        return blockUserEntity;
    }
    @Override
    public List<UserResponse> getAllBlockedUserById(Long userId){
        List<BlockUserEntity> block = blockUserRepository.findByBlocker_Id(userId);
        if(block.isEmpty()){
           return new ArrayList<>();
        }
        List<UserResponse> listBlock = new ArrayList<>();
        for(BlockUserEntity bl : block){
            listBlock.add(userConverter.convertToResponse(bl.getBlockedUser()));
        }
        return listBlock;

    }
    public void deleteBlocked(Long blockedId){
        ResponseDTO responseDTO = new ResponseDTO();
        if(!blockUserRepository.existsById(blockedId)){
            throw new RuntimeException("Block ID Not Found");
        }
        BlockUserEntity block = blockUserRepository.getReferenceById(blockedId);
        block.setBlockedUser(null);
        block.setBlocker(null);
        blockUserRepository.delete(block);
    }

}
