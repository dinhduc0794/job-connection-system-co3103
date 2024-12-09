package com.javaweb.jobconnectionsystem.listener;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
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

    @PostPersist
    @PostUpdate
    @PostRemove
    public void onRateCompanyChange(RateCompanyEntity rateCompanyEntity) {
        if (rateCompanyEntity.getCompany() != null) {
            CompanyEntity company = rateCompanyEntity.getCompany();
            double newRating = calculateAverageRating(company);
            company.setRating(newRating);
            companyRepository.save(company);
        }
    }

    private double calculateAverageRating(CompanyEntity company) {
        List<RateCompanyEntity> ratings = company.getRateCompanyEntities();
        if (ratings.isEmpty()) return 0.0;

        double totalRating = 0;
        for (RateCompanyEntity rating : ratings) {
            totalRating += rating.getRate();
        }
        return totalRating / ratings.size();
    }
}

