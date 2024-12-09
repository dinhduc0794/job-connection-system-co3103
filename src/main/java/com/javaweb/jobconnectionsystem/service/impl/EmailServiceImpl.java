package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import com.javaweb.jobconnectionsystem.repository.EmailRepository;
import com.javaweb.jobconnectionsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailRepository emailRepository;
    @Override
    public List<EmailEntity> getAllEmails() {
        return emailRepository.findAll();
    }
    @Override
    public EmailEntity addEmail(EmailEntity email) {
        if (email == null) {
            return null;
//        }
//        else if(emailRepository.findById(email.getId()).isPresent() &&
//                emailRepository.findById(email.getId()).get().getId().equals(email.getId())) {
//            return null;
        }
        return emailRepository.save(email);
    }
    @Override
    public Optional<EmailEntity> getEmailById(Long id) {
        Optional<EmailEntity> email = emailRepository.findById(id);
        if (email.isEmpty()) {
            return null;
        }
        return email;
    }
    @Override
    public EmailEntity updateEmail(Long id, EmailEntity emailDetails) {
        EmailEntity email = emailRepository.findById(id).orElseThrow(() -> new RuntimeException("email not found"));
        if (emailRepository.existsByEmail(emailDetails.getEmail())) {
            throw new RuntimeException("email already exists");
        }

        email.setEmail(emailDetails.getEmail());
        return emailRepository.save(email);
    }

    @Override
    public void deleteEmailById(Long id) {
        EmailEntity email = emailRepository.findById(id).orElseThrow(() -> new RuntimeException("Email not found"));
        emailRepository.delete(email);
    }
}
