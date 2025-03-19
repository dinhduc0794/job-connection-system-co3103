package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;

import java.util.List;
import java.util.Optional;

public interface PhoneNumberService {
    // Thêm số điện thoại
    public PhoneNumberEntity addPhoneNumber(PhoneNumberEntity phoneNumber);

    // Lấy tất cả số điện thoại
    public List<PhoneNumberEntity> getAllPhoneNumbers();

    // Tìm số điện thoại theo ID
    public Optional<PhoneNumberEntity> getPhoneNumberById(Long id);

    // Xóa số điện thoại theo ID
    public void deletePhoneNumberById(Long id);

    // Cập nhật số điện thoại
    public PhoneNumberEntity updatePhoneNumber(Long id, PhoneNumberEntity phoneNumber);
}
