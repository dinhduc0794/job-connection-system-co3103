package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.NotificationEntity;
import com.javaweb.jobconnectionsystem.model.response.NotificationReponse;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Endpoint thêm thông báo
    @PostMapping
    public ResponseEntity<NotificationEntity> addNotification(@RequestBody NotificationEntity notification) {
        NotificationEntity createdNotification = notificationService.addNotification(notification);
        if (createdNotification == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu thông báo không hợp lệ
        }
        return ResponseEntity.ok(createdNotification); // Trả về thông báo đã thêm
    }

    // Endpoint lấy tất cả thông báo
    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllNotifications(@PathVariable Long userId) {
      try {
          List<NotificationEntity> notifications = notificationService.getAllNotifications(userId);
          List<NotificationReponse> notificationDTOs = notifications.stream()
                  .map(notification -> new NotificationReponse(notification.getId(), notification.getContent()))
                  .collect(Collectors.toList());
          return ResponseEntity.ok(notificationDTOs);
      }catch (RuntimeException ex) {
          // Trả về thông báo lỗi với mã 404 hoặc mã lỗi tùy chọn
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
      }
    }

    // Endpoint lấy thông báo theo ID
//    @GetMapping("/{id}")
//    public ResponseEntity<NotificationEntity> getNotificationById(@PathVariable Long id) {
//        Optional<NotificationEntity> notification = notificationService.getNotificationById(id);
//        return notification.map(ResponseEntity::ok) // Trả về thông báo nếu tìm thấy
//                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy thông báo
//    }

    // Endpoint cập nhật thông báo
    @PutMapping("/{id}")
    public ResponseEntity<NotificationEntity> updateNotification(@PathVariable Long id, @RequestBody NotificationEntity notificationDetails) {
        try {
            NotificationEntity updatedNotification = notificationService.updateNotification(id, notificationDetails);
            return ResponseEntity.ok(updatedNotification); // Trả về thông báo đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa thông báo
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            notificationService.deleteNotificationById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Notification has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
