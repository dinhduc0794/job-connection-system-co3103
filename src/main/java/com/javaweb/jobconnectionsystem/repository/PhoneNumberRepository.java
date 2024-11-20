package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity,Long> {
    public boolean existsByPhoneNumber(String phone);
    public void deleteByPhoneNumber(String phoneNumber);
    public List<PhoneNumberEntity> findByUser_Id(Long comanyId);
}
