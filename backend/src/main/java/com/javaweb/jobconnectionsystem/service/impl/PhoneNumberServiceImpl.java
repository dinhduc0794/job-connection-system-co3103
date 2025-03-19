package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import com.javaweb.jobconnectionsystem.repository.PhoneNumberRepository;
import com.javaweb.jobconnectionsystem.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Override
    public List<PhoneNumberEntity> getAllPhoneNumbers() {
        return phoneNumberRepository.findAll();
    }

    @Override
    public PhoneNumberEntity addPhoneNumber(PhoneNumberEntity phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumberRepository.save(phoneNumber);
    }

    @Override
    public Optional<PhoneNumberEntity> getPhoneNumberById(Long id) {
        Optional<PhoneNumberEntity> phoneNumber = phoneNumberRepository.findById(id);
        if (phoneNumber.isEmpty()) {
            return null;
        }
        return phoneNumber;
    }

    @Override
    public PhoneNumberEntity updatePhoneNumber(Long id, PhoneNumberEntity phoneNumberDetails) {
        PhoneNumberEntity phoneNumber = phoneNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phone number not found"));

        // Kiểm tra nếu số điện thoại đã tồn tại (nếu cần thiết)
        if (phoneNumberRepository.existsByPhoneNumber(phoneNumberDetails.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Cập nhật thông tin số điện thoại
        phoneNumber.setPhoneNumber(phoneNumberDetails.getPhoneNumber());
        return phoneNumberRepository.save(phoneNumber);
    }

    @Override
    public void deletePhoneNumberById(Long id) {
        PhoneNumberEntity phoneNumber = phoneNumberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phone number not found"));
        phoneNumberRepository.delete(phoneNumber);
    }
}
