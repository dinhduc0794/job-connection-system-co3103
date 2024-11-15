package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.entity.WardEntity;
import com.javaweb.jobconnectionsystem.repository.ProvinceRepository;
import com.javaweb.jobconnectionsystem.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceSerciveImpl implements ProvinceService {
    @Autowired
    public ProvinceRepository provinceRepository;
    @Override
    public List<CityEntity> findCity(Long id){
        return provinceRepository.findById(id).get().getCities();
    }
    @Override
    public ProvinceEntity addProvince(ProvinceEntity newProvince){
        ProvinceEntity saveProvince=provinceRepository.save(newProvince);
        return saveProvince;
    }
}
