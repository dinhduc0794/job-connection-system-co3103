package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {
        @Autowired
        private AccountService accountService;
//
//        @GetMapping()
//        public List<AccountEntity> getAllAccounts() {
//            return accountService.getAllAccounts();
//        }
//
//        // Lấy tài khoản theo ID
//        @GetMapping("/{id}")
//        public ResponseEntity<AccountEntity> getAccountById(@PathVariable Long id) {
//            return accountService.getAccountById(id)
//                    .map(ResponseEntity::ok)
//                    .orElse(ResponseEntity.notFound().build());
//        }
//
//        // Sửa tài khoản
//        @PutMapping("/{id}")
//        public ResponseEntity<AccountEntity> updateAccount(@PathVariable Long id, @RequestBody AccountEntity accountDetails) {
//            AccountEntity updatedAccount = accountService.updateAccount(id, accountDetails);
//            return ResponseEntity.ok(updatedAccount);
//        }
//
//        // Xóa tài khoản
//        @DeleteMapping("/{id}")
//        public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
//            accountService.deleteAccount(id);
//            return ResponseEntity.noContent().build();
//        }
//    @GetMapping("/account")
//    public void addAccount(@RequestBody AccountEntity account) {
//        accountService.addAccount(account);
//
//    }

}
