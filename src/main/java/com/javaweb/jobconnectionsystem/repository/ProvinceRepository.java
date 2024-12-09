package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.model.location.ProvinceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceEntity,Long> {
    @Query("SELECT p FROM ProvinceEntity p")
    List<ProvinceDTO> findAllProvinces();
}
