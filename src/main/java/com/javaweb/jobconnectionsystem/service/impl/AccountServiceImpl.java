package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.entity.UserEntity;
import com.javaweb.jobconnectionsystem.repository.AccountRepository;
import com.javaweb.jobconnectionsystem.repository.UserRepository;
import com.javaweb.jobconnectionsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    //
    @Override
    public AccountEntity addAccount(AccountEntity account) {
        Optional<AccountEntity> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Lưu tài khoản mới nếu username chưa tồn tại
        return accountRepository.save(account);
    }
    @Override
    public Long getIdAccountByUsername(String username) {
        Optional<AccountEntity> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            return existingAccount.get().getId();
        }
        return null;
    }

    @Override
    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<AccountEntity> getAccountById(Long id) {
        AccountEntity account = accountRepository.findById(id).orElse(null);
        return Optional.ofNullable(account); // Trả về Optional thay vì null
    }

    @Override
    public AccountEntity updateAccount(Long id, AccountEntity accountDetails) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        if (accountRepository.existsByUsername(accountDetails.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        account.setUsername(accountDetails.getUsername());
        account.setPassword(accountDetails.getPassword());

        return accountRepository.save(account);
    }

    @Override
    //
    public void deleteAccount(Long id) {
        Optional<AccountEntity> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            System.out.println("Account not found with id: " + id); // Thêm log để kiểm tra
            throw new RuntimeException("Account not found");
        }
        accountRepository.delete(account.get());
    }
}
