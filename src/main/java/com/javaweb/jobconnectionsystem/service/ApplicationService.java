package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;

import java.util.List;

public interface ApplicationService {
    ApplicationEntity saveApplication(ApplicationDTO applicationDTO);
    List<ApplicationEntity> getAllApplicationByApplicantId(Long id);
    ApplicationEntity getApplicationById(Long id);
    List<ApplicationEntity> getAllApplicationByJobpostingId(Long id);
    void deleteApplicationById (Long id);
    // lam gi day em

}
