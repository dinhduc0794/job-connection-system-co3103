package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity,Long> {
    public boolean existsByPhoneNumber(String phone);
    public List<PhoneNumberEntity> findByUser_Id(Long comanyId);
    @Query("SELECT p FROM PhoneNumberEntity p WHERE p.phoneNumber = :phone")
    PhoneNumberEntity findByPhoneNumber(@Param("phone") String phone);
    @Modifying
    @Transactional
    @Query("DELETE FROM PhoneNumberEntity p WHERE p.phoneNumber = :phoneNumber")
    void deleteByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}