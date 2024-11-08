package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.ProCompanyEntity;
import com.javaweb.jobconnectionsystem.repository.ProCompanyRepository;
import com.javaweb.jobconnectionsystem.service.ProCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProCompanyServiceImpl implements ProCompanyService {
    @Autowired
    ProCompanyRepository proCompanyRepository;

    @Override
    public void saveProCompany() {
        System.out.println("ProCompany saved");
        ProCompanyEntity proCompanyEntity = new ProCompanyEntity();
        proCompanyEntity.setName("Ten cong ty");
        proCompanyEntity.setExpireDate(new Date(22-12-2022));
        proCompanyEntity.setRegistDate(new Date(22-12-2021));
        proCompanyEntity.setTaxCode("12332123");
        proCompanyEntity.setPassword("qwdadaad");
        proCompanyEntity.setUsername("congty");
        proCompanyEntity.setIsPublic(true);
        proCompanyEntity.setIsBanned(false);
        proCompanyEntity.setIsActive(true);
        proCompanyRepository.save(proCompanyEntity);
    }

}
