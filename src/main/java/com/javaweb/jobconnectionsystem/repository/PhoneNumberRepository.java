package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity,Long> {
    public boolean existsByPhoneNumber(String phone);
    public void deleteByPhoneNumber(String phoneNumber);
}
