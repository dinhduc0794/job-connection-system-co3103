package com.javaweb.jobconnectionsystem.repository.custom;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;

import java.util.List;

public interface CompanyRepositoryCustom {
    List<CompanyEntity> findAll(CompanySearchRequest params);
    int countTotalItems(CompanySearchRequest params);
}
