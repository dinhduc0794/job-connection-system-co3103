package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //thêm User
    public UserEntity addUser(UserEntity user,String phoneNumber,String email);
    // Lấy thêm user
    public List<UserEntity> getAllUsers();
    // Lấy user theo ID
    public Optional<UserEntity> getUserById(Long id);
    // Sửa use
    public UserEntity updateUser(Long id, UserEntity userDetails);
    // Xoa user
    public void deleteUserById(Long id);

}


