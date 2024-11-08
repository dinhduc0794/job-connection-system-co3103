package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.repository.AccountRepository;
import com.javaweb.jobconnectionsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountEntity addAccount(AccountEntity account) {
        return accountRepository.save(account);
    }

    @Override
    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<AccountEntity> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public AccountEntity updateAccount(Long id, AccountEntity accountDetails) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setUsername(accountDetails.getUsername());
        account.setPassword(accountDetails.getPassword());
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        AccountEntity account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }
}
