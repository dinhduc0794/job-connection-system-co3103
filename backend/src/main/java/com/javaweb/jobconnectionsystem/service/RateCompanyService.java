package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.RateCompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;

import java.util.List;

public interface RateCompanyService {
    ResponseDTO saveRate(RateCompanyDTO rateCompanyDTO);
    void deleteRate(Long id);
    List<RateCompanyDTO> getRateCompanyByApplicantId(Long applicantId);
    List<RateCompanyDTO> getRateCompanyByCompanyId(Long companyId);
    RateCompanyDTO getRateCompanyId(Long id);
}
