package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.BlockUserEntity;
import com.javaweb.jobconnectionsystem.model.dto.BlockingDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.model.response.UserResponse;
import com.javaweb.jobconnectionsystem.service.BlockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    @GetMapping("/users/{userID}")
    public ResponseEntity<?> getAllBlockings(@PathVariable Long userID) {
            List<UserResponse> blockedUsers = blockUserService.getAllBlockedUserById(userID);
            if(blockedUsers.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You don't have any blocked");
            }
            return ResponseEntity.ok(blockedUsers); // Trả về danh sách người bị chặn
    }
    @DeleteMapping("/{blockId}")
    public ResponseEntity<?> deleteBlock(@PathVariable Long blockId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("User has been unblocked"));
            blockUserService.deleteBlocked(blockId);
            return ResponseEntity.ok(responseDTO);
        }
        catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
