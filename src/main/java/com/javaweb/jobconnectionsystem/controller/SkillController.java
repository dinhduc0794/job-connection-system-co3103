package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import com.javaweb.jobconnectionsystem.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    // Endpoint thêm kỹ năng
    @PostMapping
    public ResponseEntity<SkillEntity> addSkill(@RequestBody SkillEntity skill) {
        SkillEntity createdSkill = skillService.addSkill(skill);
        if (createdSkill == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu kỹ năng không hợp lệ
        }
        return ResponseEntity.ok(createdSkill); // Trả về kỹ năng đã thêm
    }

    // Endpoint lấy tất cả kỹ năng
    @GetMapping
    public ResponseEntity<List<SkillEntity>> getAllSkills() {
        List<SkillEntity> skills = skillService.getAllSkills();
        if (skills.isEmpty()) {
            return ResponseEntity.noContent().build(); // Nếu không có kỹ năng, trả về 204 No Content
        }
        return ResponseEntity.ok(skills); // Trả về danh sách kỹ năng
    }

    // Endpoint lấy kỹ năng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<SkillEntity> getSkillById(@PathVariable Long id) {
        Optional<SkillEntity> skill = skillService.getSkillById(id);
        return skill.map(ResponseEntity::ok) // Trả về kỹ năng nếu tìm thấy
                .orElseGet(() -> ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy kỹ năng
    }

    // Endpoint cập nhật kỹ năng
    @PutMapping("/{id}")
    public ResponseEntity<SkillEntity> updateSkill(@PathVariable Long id, @RequestBody SkillEntity skillDetails) {
        try {
            SkillEntity updatedSkill = skillService.updateSkill(id, skillDetails);
            return ResponseEntity.ok(updatedSkill); // Trả về kỹ năng đã cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Nếu có lỗi, trả về lỗi
        }
    }

    // Endpoint xóa kỹ năng
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        try {
            skillService.deleteSkillById(id);
            return ResponseEntity.noContent().build(); // Trả về 204 nếu xóa thành công
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Nếu không tìm thấy kỹ năng, trả về 404 Not Found
        }
    }
}
