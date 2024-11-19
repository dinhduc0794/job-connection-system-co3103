package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.WardEntity;
import com.javaweb.jobconnectionsystem.model.dto.WardDTO;
import com.javaweb.jobconnectionsystem.repository.CityRepository;
import com.javaweb.jobconnectionsystem.repository.WardRepository;
import com.javaweb.jobconnectionsystem.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    @Override
    public WardEntity addWard(WardDTO wardDTO){
        WardEntity wardEntity = new WardEntity();
        wardEntity.setId(wardDTO.getId());
        wardEntity.setName(wardDTO.getName());
        wardEntity.setCity(cityRepository.findById(wardDTO.getId()).get());

        wardRepository.save(wardEntity);
        return wardEntity;
    }
}