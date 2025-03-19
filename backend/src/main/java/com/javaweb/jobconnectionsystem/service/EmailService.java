package com.javaweb.jobconnectionsystem.service;


import com.javaweb.jobconnectionsystem.entity.EmailEntity;

import java.util.List;
import java.util.Optional;

public interface EmailService {
    //add email
    public EmailEntity addEmail(EmailEntity email);
    //find all email
    public List<EmailEntity> getAllEmails();
    //  find by id
    public Optional<EmailEntity> getEmailById(Long id);
    // delete by id
    public void deleteEmailById(Long id);
    // update email
    public EmailEntity updateEmail(Long id,EmailEntity email);
}
