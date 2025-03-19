package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity,Long> {
}
