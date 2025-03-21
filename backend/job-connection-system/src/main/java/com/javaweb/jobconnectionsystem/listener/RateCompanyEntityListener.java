package com.javaweb.jobconnectionsystem.listener;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.service.CompanyService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RateCompanyEntityListener {

    @Autowired
    private CompanyRepository companyRepository;

    private static CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        RateCompanyEntityListener.companyService = companyService;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void onRateCompanyChange(RateCompanyEntity rateCompanyEntity) {
        if (rateCompanyEntity.getCompany() != null) {
            CompanyEntity company = rateCompanyEntity.getCompany();
            companyService.updateCompanyRating(company);
        }
    }
}

