package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.FieldEntity;
import com.javaweb.jobconnectionsystem.repository.FieldRepository;
import com.javaweb.jobconnectionsystem.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public FieldEntity addField(FieldEntity field) {
        if (field == null) {
            return null;
        }
        return fieldRepository.save(field);
    }

    @Override
    public List<FieldEntity> getAllFields() {
        return fieldRepository.findAll();
    }

    @Override
    public Optional<FieldEntity> getFieldById(Long id) {
        return fieldRepository.findById(id);
    }

    @Override
    public FieldEntity updateField(Long id, FieldEntity fieldDetails) {
        FieldEntity field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        field.setName(fieldDetails.getName());
        field.setJobTypes(fieldDetails.getJobTypes());

        return fieldRepository.save(field);
    }

    @Override
    public void deleteFieldById(Long id) {
        FieldEntity field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        fieldRepository.delete(field);
    }
}
