package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.model.location.ProvinceDTO;
import com.javaweb.jobconnectionsystem.repository.CityRepository;
import com.javaweb.jobconnectionsystem.repository.ProvinceRepository;
import com.javaweb.jobconnectionsystem.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinceSerciveImpl implements ProvinceService {
    @Autowired
    public ProvinceRepository provinceRepository;
    @Autowired
    public CityRepository cityrepository;

    @Override
    public List<ProvinceDTO> findAllLocations() {
        return provinceRepository.findAllProvinces();
    }

    @Override
    public List<CityEntity> findCity(Long id) {
        return provinceRepository.findById(id).get().getCities();
    }

    @Override
    public ProvinceEntity addProvince(ProvinceEntity newProvince) {
        if (newProvince.getCities().isEmpty()) {
            return provinceRepository.save(newProvince);
        } else {
            List<CityEntity> validatedCities = new ArrayList<>();
            for (CityEntity city : newProvince.getCities()) {
                CityEntity newcity = new CityEntity();
                newcity.setProvince(newProvince);
                newcity.setName(city.getName());
                cityrepository.save(newcity);
                validatedCities.add(newcity);
            }
            newProvince.setCities(validatedCities);
        }
        return provinceRepository.save(newProvince);
    }
}
