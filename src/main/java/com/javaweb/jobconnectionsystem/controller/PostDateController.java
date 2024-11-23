package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.PostDateEntity;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.PostDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postdates")
public class PostDateController {

    @Autowired
    private PostDateService postDateService;

    // Endpoint thêm ngày đăng
    @PostMapping
    public ResponseEntity<PostDateEntity> addPostDate(@RequestBody PostDateEntity postDate) {
        PostDateEntity createdPostDate = postDateService.addPostDate(postDate);
        if (createdPostDate == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu ngày đăng không hợp lệ
        }
        return ResponseEntity.ok(createdPostDate); // Trả về ngày đăng đã thêm
    }

    // Endpoint lấy tất cả ngày đăng
    @GetMapping
    public ResponseEntity<List<PostDateEntity>> getAllPostDates() {
        List<PostDateEntity> postDates = postDateService.getAllPostDates();
        if (postDates.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có ngày đăng, trả về 204 No Content
        }
        return ResponseEntity.ok(postDates); // Trả về danh sách ngày đăng
    }

    // Endpoint lấy ngày đăng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PostDateEntity> getPostDateById(@PathVariable Long id) {
        Optional<PostDateEntity> postDate = postDateService.getPostDateById(id);
        return postDate.map(ResponseEntity::ok) // Trả về ngày đăng nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy ngày đăng
    }

    // Endpoint cập nhật ngày đăng
    @PutMapping("/{id}")
    public ResponseEntity<PostDateEntity> updatePostDate(@PathVariable Long id, @RequestBody PostDateEntity postDateDetails) {
        try {
            PostDateEntity updatedPostDate = postDateService.updatePostDate(id, postDateDetails);
            return ResponseEntity.ok(updatedPostDate); // Trả về ngày đăng đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa ngày đăng
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostDate(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            postDateService.deletePostDateById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Post date has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
