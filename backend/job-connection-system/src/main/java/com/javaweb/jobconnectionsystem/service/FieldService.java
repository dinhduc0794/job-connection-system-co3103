package com.javaweb.jobconnectionsystem.service;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;

import java.util.List;
import java.util.Optional;

public interface FieldService {
    // Thêm lĩnh vực
    FieldEntity addField(FieldEntity field);

    // Lấy tất cả lĩnh vực
    List<FieldEntity> getAllFields();

    // Lấy lĩnh vực theo ID
    Optional<FieldEntity> getFieldById(Long id);

    // Cập nhật lĩnh vực
    FieldEntity updateField(Long id, FieldEntity fieldDetails);

    // Xóa lĩnh vực theo ID
    void deleteFieldById(Long id);
}
