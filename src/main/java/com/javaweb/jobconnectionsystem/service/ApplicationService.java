package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.ApplicationEntity;
import com.javaweb.jobconnectionsystem.model.dto.ApplicationDTO;

import java.util.List;

public interface ApplicationService {
    public ApplicationEntity saveApplication(ApplicationDTO applicationDTO);
    public List<ApplicationEntity> getAllApplication();
    public void DeleteApplication (Long applicationID);
}
