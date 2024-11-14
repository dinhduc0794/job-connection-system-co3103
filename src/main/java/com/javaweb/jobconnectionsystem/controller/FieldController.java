package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;
import com.javaweb.jobconnectionsystem.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    // Endpoint thêm lĩnh vực
    @PostMapping
    public ResponseEntity<FieldEntity> addField(@RequestBody FieldEntity field) {
        FieldEntity createdField = fieldService.addField(field);
        if (createdField == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu lĩnh vực không hợp lệ
        }
        return ResponseEntity.ok(createdField); // Trả về lĩnh vực đã thêm
    }

    // Endpoint lấy tất cả lĩnh vực
    @GetMapping
    public ResponseEntity<List<FieldEntity>> getAllFields() {
        List<FieldEntity> fields = fieldService.getAllFields();
        if (fields.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có lĩnh vực, trả về 204 No Content
        }
        return ResponseEntity.ok(fields); // Trả về danh sách lĩnh vực
    }

    // Endpoint lấy lĩnh vực theo ID
    @GetMapping("/{id}")
    public ResponseEntity<FieldEntity> getFieldById(@PathVariable Long id) {
        Optional<FieldEntity> field = fieldService.getFieldById(id);
        return field.map(ResponseEntity::ok) // Trả về lĩnh vực nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy lĩnh vực
    }

    // Endpoint cập nhật lĩnh vực
    @PutMapping("/{id}")
    public ResponseEntity<FieldEntity> updateField(@PathVariable Long id, @RequestBody FieldEntity fieldDetails) {
        try {
            FieldEntity updatedField = fieldService.updateField(id, fieldDetails);
            return ResponseEntity.ok(updatedField); // Trả về lĩnh vực đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa lĩnh vực
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        try {
            fieldService.deleteFieldById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy lĩnh vực, trả về 404 Not Found
        }
    }
}
