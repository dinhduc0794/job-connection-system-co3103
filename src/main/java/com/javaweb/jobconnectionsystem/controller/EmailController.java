package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import com.javaweb.jobconnectionsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint thêm email
    @PostMapping
    public ResponseEntity<EmailEntity> addEmail(@RequestBody EmailEntity email) {
        EmailEntity createdEmail = emailService.addEmail(email);
        if (createdEmail == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu email đã tồn tại
        }
        return ResponseEntity.ok(createdEmail); // Trả về email đã thêm
    }

    // Endpoint lấy tất cả email
    @GetMapping
    public ResponseEntity<List<EmailEntity>> getAllEmails() {
        List<EmailEntity> emails = emailService.getAllEmails();
        if (emails.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có email, trả về 204 No Content
        }
        return ResponseEntity.ok(emails); // Trả về danh sách email
    }

    // Endpoint lấy email theo ID
    @GetMapping("/{id}")
    public ResponseEntity<EmailEntity> getEmailById(@PathVariable Long id) {
        Optional<EmailEntity> email = emailService.getEmailById(id);
        return email.map(ResponseEntity::ok) // Trả về email nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy email
    }

    // Endpoint cập nhật email
    @PutMapping("/{id}")
    public ResponseEntity<EmailEntity> updateEmail(@PathVariable Long id, @RequestBody EmailEntity emailDetails) {
        try {
            EmailEntity updatedEmail = emailService.updateEmail(id, emailDetails);
            return ResponseEntity.ok(updatedEmail); // Trả về email đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: email đã tồn tại), trả về lỗi
        }
    }

    // Endpoint xóa email
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        try {
            emailService.deleteEmailById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy email, trả về 404 Not Found
        }
    }
}
