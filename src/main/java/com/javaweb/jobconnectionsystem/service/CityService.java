package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.entity.WardEntity;

import java.util.List;

public interface CityService {
    public ProvinceEntity findProvince(Long ProvinceID);
    public CityEntity AddCity (CityEntity city);
    public List<WardEntity> findWard(Long WardId);
}
