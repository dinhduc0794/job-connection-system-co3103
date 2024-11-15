package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.entity.WardEntity;
import com.javaweb.jobconnectionsystem.repository.CityRepository;
import com.javaweb.jobconnectionsystem.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;
    @Override
    public ProvinceEntity findProvince(Long ProvinceID){
         return cityRepository.findByProvinceId(ProvinceID);
    }
    @Override
    public CityEntity AddCity (CityEntity city){
        return cityRepository.save(city);
    }
    @Override
    public List<WardEntity> findWard(Long WardId){
        return cityRepository.findById(WardId).get().getWards();
    }
}
