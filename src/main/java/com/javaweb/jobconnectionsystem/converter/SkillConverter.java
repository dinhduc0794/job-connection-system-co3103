package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.JobTypeEntity;
import com.javaweb.jobconnectionsystem.entity.SkillEntity;
import com.javaweb.jobconnectionsystem.model.dto.SkillDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.JobTypeRepository;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillConverter {
    @Autowired
    private ModelMapper modelMapper ;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private JobTypeRepository jobTypeRepository;
    public SkillEntity toSkillEntity(SkillDTO skillDTO){
        SkillEntity skillEntity = modelMapper.map(skillDTO, SkillEntity.class);
        List<Long> applicantId = skillDTO.getApplicantIds();
        if(!applicantId.isEmpty()){
            for(Long id: applicantId){
                if(applicantRepository.existsById(id))
                skillEntity.getApplicants().add(applicantRepository.findById(id).get());
                applicantRepository.findById(id).get().getSkills().add(skillEntity);
            }
        }
        if(jobTypeRepository.existsById(skillDTO.getJobTypeId())) {
            skillEntity.setJobType(jobTypeRepository.getReferenceById(skillDTO.getJobTypeId()));
        }
        return skillEntity ;
    }
}
