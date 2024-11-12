package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity,Long> {
}
