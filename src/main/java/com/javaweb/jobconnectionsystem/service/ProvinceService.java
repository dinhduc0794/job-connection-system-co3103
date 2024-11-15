package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;

import java.util.List;

public interface ProvinceService {
    public ProvinceEntity addProvince(ProvinceEntity newProvince);
    public List<CityEntity> findCity(Long ProvinceId);
}
