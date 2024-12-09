package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.model.location.ProvinceDTO;

import java.util.List;

public interface ProvinceService {
    public List<ProvinceDTO> findAllLocations();
    public ProvinceEntity addProvince(ProvinceEntity newProvince);
    public List<CityEntity> findCity(Long ProvinceId);
}
