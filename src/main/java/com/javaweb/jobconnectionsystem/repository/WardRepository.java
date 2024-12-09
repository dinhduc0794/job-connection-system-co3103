package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<WardEntity,Long> {
    List<WardEntity> findAllByCityId(Long cityId);
    Optional<WardEntity> findById(Long id);
}
