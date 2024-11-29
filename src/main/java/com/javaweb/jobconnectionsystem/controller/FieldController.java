package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class FieldController {

    @Autowired
    private FieldService fieldService;
    // Endpoint lấy tất cả lĩnh vực
    @GetMapping("/public/fields")
    public ResponseEntity<List<FieldEntity>> getAllFields() {
        List<FieldEntity> fields = fieldService.getAllFields();
        if (fields.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có lĩnh vực, trả về 204 No Content
        }
        return ResponseEntity.ok(fields); // Trả về danh sách lĩnh vực
    }

    // Endpoint lấy lĩnh vực theo ID
    @GetMapping("/public/fields/{id}")
    public ResponseEntity<FieldEntity> getFieldById(@PathVariable Long id) {
        Optional<FieldEntity> field = fieldService.getFieldById(id);
        return field.map(ResponseEntity::ok) // Trả về lĩnh vực nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy lĩnh vực
    }

    // Endpoint thêm lĩnh vực
    @PostMapping("/fields")
    public ResponseEntity<FieldEntity> addField(@RequestBody FieldEntity field) {
        FieldEntity createdField = fieldService.addField(field);
        if (createdField == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu lĩnh vực không hợp lệ
        }
        return ResponseEntity.ok(createdField); // Trả về lĩnh vực đã thêm
    }


    // Endpoint cập nhật lĩnh vực
    @PutMapping("/fields/{id}")
    public ResponseEntity<FieldEntity> updateField(@PathVariable Long id, @RequestBody FieldEntity fieldDetails) {
        try {
            FieldEntity updatedField = fieldService.updateField(id, fieldDetails);
            return ResponseEntity.ok(updatedField); // Trả về lĩnh vực đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa lĩnh vực
    @DeleteMapping("/fields/{id}")
    public ResponseEntity<?> deleteField(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            fieldService.deleteFieldById(id);
            responseDTO.setMessage("Delete successfully");
            responseDTO.setDetail(Collections.singletonList("Field has been deleted"));
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            responseDTO.setMessage("Internal server error");
            responseDTO.setDetail(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
