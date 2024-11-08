package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.UserEntity;
import com.javaweb.jobconnectionsystem.repository.UserRepository;
import com.javaweb.jobconnectionsystem.service.AccountService;
import com.javaweb.jobconnectionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser() {
        UserEntity user = new UserEntity();
        user.setUsername("hao");
        user.setPassword("123");
        user.setDescription("dsđasa");
        user.setIsActive(true);
        user.setIsBanned(false);
        user.setIsPublic(true);
        userRepository.save(user);
    }
}
