package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.ProCompanyEntity;
import com.javaweb.jobconnectionsystem.repository.ProCompanyRepository;
import com.javaweb.jobconnectionsystem.service.ProCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProCompanyServiceImpl implements ProCompanyService {

    @Autowired
    private ProCompanyRepository proCompanyRepository;

    @Override
    public ProCompanyEntity addProCompany(ProCompanyEntity proCompany) {
        return proCompanyRepository.save(proCompany); // Thêm một công ty  mới
    }

    @Override
    public List<ProCompanyEntity> getAllProCompanies() {
        return proCompanyRepository.findAll(); // Lấy tất cả các công ty
    }

    @Override
    public Optional<ProCompanyEntity> getProCompanyById(Long id) {
        Optional<ProCompanyEntity> proCompany = proCompanyRepository.findById(id);
        if (proCompany.isEmpty()) {
            return Optional.empty(); // Trả về Optional.empty nếu không tìm thấy công ty
        }
        return proCompany; // Trả về công ty  tìm được
    }

    @Override
    public ProCompanyEntity updateProCompany(Long id, ProCompanyEntity proCompanyDetails) {
        ProCompanyEntity proCompany = proCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProCompany not found"));

        // Cập nhật các thuộc tính của công ty nếu cần
        proCompany.setExpireDate(proCompanyDetails.getExpireDate());
        proCompany.setRegistDate(proCompanyDetails.getRegistDate());

        return proCompanyRepository.save(proCompany); // Lưu công ty đã cập nhật
    }

    @Override
    public void deleteProCompanyById(Long id) {
        ProCompanyEntity proCompany = proCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProCompany not found"));
        proCompanyRepository.delete(proCompany); // Xóa công ty  theo ID
    }
}
