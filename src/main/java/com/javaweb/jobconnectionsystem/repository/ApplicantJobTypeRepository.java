package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ApplicantJobtypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApplicantJobTypeRepository extends JpaRepository<ApplicantJobtypeEntity,Long> {

}
