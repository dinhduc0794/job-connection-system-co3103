package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import com.javaweb.jobconnectionsystem.repository.SkillRepository;
import com.javaweb.jobconnectionsystem.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public SkillEntity addSkill(SkillEntity skill) {
        if (skill == null) {
            return null;
        }
        return skillRepository.save(skill);
    }

    @Override
    public List<SkillEntity> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<SkillEntity> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    @Override
    public SkillEntity updateSkill(Long id, SkillEntity skillDetails) {
        SkillEntity skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        skill.setName(skillDetails.getName());
        skill.setJobType(skillDetails.getJobType());

        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkillById(Long id) {
        SkillEntity skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        skillRepository.delete(skill);
    }
}