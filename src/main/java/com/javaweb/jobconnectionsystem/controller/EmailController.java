package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public void saveEmail() {
        System.out.println("Save email at controller");
        emailService.save();
    }
}
