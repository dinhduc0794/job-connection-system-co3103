package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.converter.SkillConverter;
import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import com.javaweb.jobconnectionsystem.model.dto.SkillDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
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
    @Autowired
    private SkillConverter skillConverter;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Override
    public SkillEntity saveSkill(SkillDTO skill) {
            SkillEntity skillEntity =skillConverter.toSkillEntity(skill);
            return skillRepository.save(skillEntity);
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
