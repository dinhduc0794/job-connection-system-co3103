package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import com.javaweb.jobconnectionsystem.model.dto.SkillDTO;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    // Thêm kỹ năng
    SkillEntity saveSkill(SkillDTO skillDTO);

    // Lấy tất cả kỹ năng
    List<SkillEntity> getAllSkills();

    // Lấy kỹ năng theo ID
    Optional<SkillEntity> getSkillById(Long id);

    // Cập nhật kỹ năng
    SkillEntity updateSkill(Long id, SkillEntity skillDetails);

    // Xóa kỹ năng theo ID
    void deleteSkillById(Long id);
}
