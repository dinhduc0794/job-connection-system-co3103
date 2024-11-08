package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import com.javaweb.jobconnectionsystem.repository.EmailRepository;
import com.javaweb.jobconnectionsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailRepository emailRepository;

    @Override
    public void save() {
        System.out.println("Save email");
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmail("hao@gmail.com");
        emailRepository.save(emailEntity);
    }
}
