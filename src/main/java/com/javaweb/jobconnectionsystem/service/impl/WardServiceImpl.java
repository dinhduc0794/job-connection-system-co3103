package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.WardEntity;
import com.javaweb.jobconnectionsystem.repository.CityRepository;
import com.javaweb.jobconnectionsystem.repository.WardRepository;
import com.javaweb.jobconnectionsystem.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<WardEntity> findWardbyCity(Long CityID){
         return wardRepository.findAllByCityId(CityID);
    }

}
