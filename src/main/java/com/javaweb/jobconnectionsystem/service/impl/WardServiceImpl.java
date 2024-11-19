package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.WardEntity;
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

    @Override
    public List<WardEntity> findWardbyCity(Long CityID){
         return wardRepository.findAllBycityId(CityID);
    }
    @Override
    public WardEntity AddWard(WardEntity newward){
        WardEntity newWard = new WardEntity();

       return  wardRepository.save(newWard);
    }



}
