package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.BlockingDTO;
import com.javaweb.jobconnectionsystem.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blockings")
public class BlockUserController {
    @Autowired
    private BlockingService blockUserService; // Giả sử bạn có BlockUserService để xử lý logic.

    @PostMapping
    public ResponseEntity<?> saveBlocking(@RequestBody BlockingDTO blockingDTO) {
        try {
            BlockUserEntity savedBlock = blockUserService.saveBlocking(blockingDTO);
            return ResponseEntity.ok(savedBlock);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu xảy ra exception trong service
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{UserID}")
    public ResponseEntity<?> getAllBlockings(@PathVariable Long UserID) {

    }



}
