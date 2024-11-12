package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import com.javaweb.jobconnectionsystem.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/phonenumber")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    // Endpoint thêm số điện thoại
    @PostMapping
    public ResponseEntity<PhoneNumberEntity> addPhoneNumber(@RequestBody PhoneNumberEntity phoneNumber) {
        PhoneNumberEntity createdPhoneNumber = phoneNumberService.addPhoneNumber(phoneNumber);
        if (createdPhoneNumber == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu số điện thoại đã tồn tại hoặc không hợp lệ
        }
        return ResponseEntity.ok(createdPhoneNumber); // Trả về số điện thoại đã thêm
    }

    // Endpoint lấy tất cả số điện thoại
    @GetMapping
    public ResponseEntity<List<PhoneNumberEntity>> getAllPhoneNumbers() {
        List<PhoneNumberEntity> phoneNumbers = phoneNumberService.getAllPhoneNumbers();
        if (phoneNumbers.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có số điện thoại, trả về 204 No Content
        }
        return ResponseEntity.ok(phoneNumbers); // Trả về danh sách số điện thoại
    }

    // Endpoint lấy số điện thoại theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PhoneNumberEntity> getPhoneNumberById(@PathVariable Long id) {
        Optional<PhoneNumberEntity> phoneNumber = phoneNumberService.getPhoneNumberById(id);
        return phoneNumber.map(ResponseEntity::ok) // Trả về số điện thoại nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy số điện thoại
    }

    // Endpoint cập nhật số điện thoại
    @PutMapping("/{id}")
    public ResponseEntity<PhoneNumberEntity> updatePhoneNumber(@PathVariable Long id, @RequestBody PhoneNumberEntity phoneNumberDetails) {
        try {
            PhoneNumberEntity updatedPhoneNumber = phoneNumberService.updatePhoneNumber(id, phoneNumberDetails);
            return ResponseEntity.ok(updatedPhoneNumber); // Trả về số điện thoại đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: số điện thoại đã tồn tại), trả về lỗi
        }
    }

    // Endpoint xóa số điện thoại
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePhoneNumber(@PathVariable Long id) {
        try {
            phoneNumberService.deletePhoneNumberById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy số điện thoại, trả về 404 Not Found
        }
    }
}
