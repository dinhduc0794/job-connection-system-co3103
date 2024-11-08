package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.service.ProCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProCompanyController {
    @Autowired
    ProCompanyService proCompanyService;

    @GetMapping("/saveProCompany")
    public void saveProCompany() {
        proCompanyService.saveProCompany();
    }
}
