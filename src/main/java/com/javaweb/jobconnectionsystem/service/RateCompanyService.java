package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;

public interface RateCompanyService {
    public ResponseDTO saveRate(RateCompanyDTO rateCompanyDTO);
    public void deleteRate(Long id);
}
