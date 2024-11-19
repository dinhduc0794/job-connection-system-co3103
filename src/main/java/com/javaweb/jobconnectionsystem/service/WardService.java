package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.WardEntity;

import java.util.List;

public interface WardService {
    public List<WardEntity> findWardbyCity(Long CityID);
    public WardEntity addWard (WardEntity wardEntity, Long cityId);
}
