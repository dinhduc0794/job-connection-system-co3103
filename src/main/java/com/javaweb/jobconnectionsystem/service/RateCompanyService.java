package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;

public interface RateCompanyService {
    public RateCompanyEntity saveRate(RateCompanyDTO rateCompanyDTO);

}
