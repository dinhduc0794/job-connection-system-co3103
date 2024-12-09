package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.CertificationEntity;
import com.javaweb.jobconnectionsystem.repository.CertificationRepository;
import com.javaweb.jobconnectionsystem.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificationServiceImpl implements CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;

    @Override
    public List<CertificationEntity> getAllCertifications() {
        return certificationRepository.findAll();
    }

    @Override
    public CertificationEntity addCertification(CertificationEntity certification) {
        if (certification == null) {
            return null;
        }
        return certificationRepository.save(certification);
    }

    @Override
    public Optional<CertificationEntity> getCertificationById(Long id) {
        Optional<CertificationEntity> certification = certificationRepository.findById(id);
        if (certification.isEmpty()) {
            return Optional.empty();
        }
        return certification;
    }

    @Override
    public CertificationEntity updateCertification(Long id, CertificationEntity certificationDetails) {
        CertificationEntity certification = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        certification.setName(certificationDetails.getName());
        certification.setLevel(certificationDetails.getLevel());
        certification.setDescription(certificationDetails.getDescription());
        certification.setProof(certificationDetails.getProof());
        certification.setStartDate(certificationDetails.getStartDate());
        certification.setEndDate(certificationDetails.getEndDate());

        return certificationRepository.save(certification);
    }

    @Override
    public void deleteCertificationById(Long id) {
        CertificationEntity certification = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found"));
        certificationRepository.delete(certification);
    }
}
