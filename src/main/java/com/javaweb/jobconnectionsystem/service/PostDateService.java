package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.PostDateEntity;

import java.util.List;
import java.util.Optional;

public interface PostDateService {
    // Thêm ngày đăng
    PostDateEntity addPostDate(PostDateEntity postDate);

    // Lấy tất cả ngày đăng
    List<PostDateEntity> getAllPostDates();

    // Lấy ngày đăng theo ID
    Optional<PostDateEntity> getPostDateById(Long id);

    // Cập nhật ngày đăng
    PostDateEntity updatePostDate(Long id, PostDateEntity postDate);

    // Xóa ngày đăng theo ID
    void deletePostDateById(Long id);
}
