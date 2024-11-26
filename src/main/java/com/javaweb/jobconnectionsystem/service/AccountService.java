package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    //thêm tài khoản
    public AccountEntity addAccount(AccountEntity account);
    // Lấy toàn bộ tài khoản

    public List<AccountEntity> getAllAccounts();
    // Lấy tài khoản theo ID
    public Optional<AccountEntity> getAccountById(Long id);
    // Sửa tài khoản
    public AccountEntity updateAccount(Long id, AccountEntity accountDetails);
    // Xoa tai khoan
    public void deleteAccount(Long id);
    public Long getIdAccountByUsername(String username);
}
