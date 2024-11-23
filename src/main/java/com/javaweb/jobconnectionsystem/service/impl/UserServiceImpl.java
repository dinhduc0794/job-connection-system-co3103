package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.entity.EmailEntity;
import com.javaweb.jobconnectionsystem.entity.PhoneNumberEntity;
import com.javaweb.jobconnectionsystem.entity.UserEntity;
import com.javaweb.jobconnectionsystem.repository.AccountRepository;
import com.javaweb.jobconnectionsystem.repository.EmailRepository;
import com.javaweb.jobconnectionsystem.repository.PhoneNumberRepository;
import com.javaweb.jobconnectionsystem.repository.UserRepository;
import com.javaweb.jobconnectionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private AccountRepository accountRepository;

    // Thêm người dùng mới
    @Override
    public UserEntity addUser(UserEntity user, String phoneNumber, String email) {
        Optional<AccountEntity> existingAccount = accountRepository.findByUsername(user.getUsername());

        if (existingAccount.isPresent()) {
            throw new RuntimeException("Username already exists");

        } else {

            UserEntity savedUser = userRepository.save(user);

            PhoneNumberEntity newPhone = new PhoneNumberEntity();
            newPhone.setPhoneNumber(phoneNumber);
            newPhone.setUser(savedUser);

            EmailEntity newEmail = new EmailEntity();
            newEmail.setEmail(email);
            newEmail.setUser(savedUser);

            savedUser.getPhoneNumbers().add(newPhone);
            savedUser.getEmails().add(newEmail);

            return savedUser;
        }
    }
    // Lấy tất cả người dùng
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy người dùng theo ID
    @Override
    public Optional<UserEntity> getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        return Optional.ofNullable(user); // Trả về Optional nếu tìm thấy, nếu không thì trả về null
    }

    // Cập nhật thông tin người dùng
    @Override
    public UserEntity updateUser(Long id, UserEntity userDetails) {
        // Tìm người dùng cần cập nhật
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDescription(userDetails.getDescription());
        user.setIsPublic(userDetails.getIsPublic());
        user.setIsBanned(userDetails.getIsBanned());
        // cập nhật sdt
        user.getPhoneNumbers().clear(); // Xóa các số cũ
        user.getPhoneNumbers().addAll(userDetails.getPhoneNumbers()); // Thêm các số mới
        // Cập nhật email
        user.getEmails().clear(); // Xóa các email cũ
        user.getEmails().addAll(userDetails.getEmails()); // Thêm các email mới
        // Cập nhật thông tin người dùng
        return userRepository.save(user);  // Lưu lại người dùng đã cập nhật
    }

    // Xóa người dùng
    @Override
    public void deleteUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        userRepository.delete(user.get());  // Xóa người dùng khỏi cơ sở dữ liệu
    }
}
