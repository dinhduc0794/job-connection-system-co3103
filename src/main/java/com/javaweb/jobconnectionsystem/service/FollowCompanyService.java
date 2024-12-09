package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.FollowCompanyEntity;
import com.javaweb.jobconnectionsystem.model.dto.FollowCompanyDTO;

public interface FollowCompanyService {
    public FollowCompanyEntity saveFollowCompany (FollowCompanyDTO followCompanyDTO);
    public FollowCompanyEntity deleteFollowCompanyById (Long id);
}
