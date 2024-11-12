package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

        @Autowired
        private AccountService accountService;

        // Endpoint thêm tài khoản mới
        @PostMapping
        public ResponseEntity<AccountEntity> addAccount(@RequestBody AccountEntity account) {
                try {
                        AccountEntity createdAccount = accountService.addAccount(account);
                        return ResponseEntity.ok(createdAccount); // Trả về tài khoản đã thêm
                } catch (RuntimeException e) {
                        // Bắt lỗi nếu username đã tồn tại và trả về mã lỗi 400
                        return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu tài khoản đã tồn tại
                }
        }

        // Endpoint lấy tất cả tài khoản
        @GetMapping
        public ResponseEntity<List<AccountEntity>> getAllAccounts() {
                List<AccountEntity> accounts = accountService.getAllAccounts();
                if (accounts.isEmpty()) {
                        return ResponseEntity.noContent().build(); // Nếu không có tài khoản, trả về 204 No Content
                }
                return ResponseEntity.ok(accounts); // Trả về danh sách tài khoản
        }

        // Endpoint lấy tài khoản theo ID
        @GetMapping("/{id}")
        public ResponseEntity<AccountEntity> getAccountById(@PathVariable Long id) {
                Optional<AccountEntity> account = accountService.getAccountById(id);
                return account.map(ResponseEntity::ok) // Trả về tài khoản nếu tìm thấy
                        .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy tài khoản
        }

        // Endpoint cập nhật tài khoản
        @PutMapping("/{id}")
        public ResponseEntity<AccountEntity> updateAccount(@PathVariable Long id, @RequestBody AccountEntity accountDetails) {
                try {
                        AccountEntity updatedAccount = accountService.updateAccount(id, accountDetails);
                        return ResponseEntity.ok(updatedAccount); // Trả về tài khoản đã cập nhật
                } catch (RuntimeException e) {
                        return ResponseEntity.badRequest().body(null); // Nếu có lỗi (ví dụ: username đã tồn tại), trả về lỗi
                }
        }

        // Endpoint xóa tài khoản
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
                try {
                        accountService.deleteAccount(id);
                        return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
                } catch (RuntimeException e) {
                        return ResponseEntity.notFound().build(); // Nếu không tìm thấy tài khoản, trả về 404 Not Found
                }
        }
}