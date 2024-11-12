package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.AccountEntity;
import com.javaweb.jobconnectionsystem.entity.UserEntity;
import com.javaweb.jobconnectionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Thêm người dùng mới
    @PostMapping("/add")
    public ResponseEntity<UserEntity> addUser(@RequestBody UserEntity user,String phone,String email) {
        UserEntity createdUser = userService.addUser(user,phone,email);
        return ResponseEntity.ok(createdUser); // Trả về người dùng đã thêm
    }
    @PostMapping("/addnophonenoemail")
    public ResponseEntity<UserEntity> addUserNophoneNoemail(@RequestBody UserEntity user) {

        try {
            UserEntity createdUser = userService.addUserNophoneNoemail(user);
            return ResponseEntity.ok(createdUser); // Trả về tài khoản đã thêm
        } catch (RuntimeException e) {
            // Bắt lỗi nếu username đã tồn tại và trả về mã lỗi 400
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu tài khoản đã tồn tại
        }
    }
    @PostMapping("/addnophone")
    public ResponseEntity<UserEntity> addUserPhone(@RequestBody UserEntity user,String phone) {
        try {
            UserEntity createdUser = userService.addUserPhone(user,phone);
            return ResponseEntity.ok(createdUser); // Trả về tài khoản đã thêm
        } catch (RuntimeException e) {
            // Bắt lỗi nếu username đã tồn tại và trả về mã lỗi 400
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu tài khoản đã tồn tại
        }
    }

    // Lấy tất cả người dùng
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có người dùng, trả về 204 No Content
        }
        return ResponseEntity.ok(users); // Trả về danh sách người dùng
    }

    // Lấy người dùng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok) // Trả về người dùng nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy người dùng
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity userDetails) {
        try {
            UserEntity updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser); // Trả về người dùng đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy người dùng, trả về 404 Not Found
        }
    }

    // Xóa người dùng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy người dùng, trả về 404 Not Found
        }
    }
}
