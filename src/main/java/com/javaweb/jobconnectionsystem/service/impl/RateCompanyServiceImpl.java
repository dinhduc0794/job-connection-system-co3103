package com.javaweb.jobconnectionsystem.service.impl;

import com.javaweb.jobconnectionsystem.entity.*;
import com.javaweb.jobconnectionsystem.model.dto.RateCompanyDTO;
import com.javaweb.jobconnectionsystem.model.response.ResponseDTO;
import com.javaweb.jobconnectionsystem.repository.ApplicantRepository;
import com.javaweb.jobconnectionsystem.repository.CompanyRepository;
import com.javaweb.jobconnectionsystem.repository.NotificationRepository;
import com.javaweb.jobconnectionsystem.repository.RateCompanyRepository;
import com.javaweb.jobconnectionsystem.service.RateCompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateCompanyServiceImpl implements RateCompanyService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RateCompanyRepository rateCompanyRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public ResponseDTO saveRate(RateCompanyDTO rateCompanyDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        RateCompanyEntity rateCompanyEntity = modelMapper.map(rateCompanyDTO, RateCompanyEntity.class);

        ApplicantEntity applicantEntity = applicantRepository.findById(rateCompanyDTO.getApplicantId()).get();
        CompanyEntity companyEntity = companyRepository.findById(rateCompanyDTO.getCompanyId()).get();

        if(rateCompanyDTO.getId() != null || rateCompanyRepository.existsByApplicantAndCompany(applicantEntity, companyEntity)) {
            responseDTO.setMessage("Cập nhật đánh giá thành công");
        }
        else {
            responseDTO.setMessage("Đánh giá thành công");
        }

        if(rateCompanyDTO.getApplicantId() != null) {
            rateCompanyEntity.setApplicant(applicantEntity);
        }
        if(rateCompanyDTO.getCompanyId() != null) {
            rateCompanyEntity.setCompany(companyEntity);
        }

        // Tính toán lại rating của công ty sau khi có đánh giá mới
        Integer newRating = rateCompanyDTO.getRate().getValue();
        Double oldRating = companyEntity.getRating();
        int numberOfRatings = companyEntity.getRateCompanyEntities().size(); // Số lượng đánh giá hiện tại từ danh sách "applicantRateCompanyEntities"
        Double totalRating = oldRating * numberOfRatings + newRating;
        companyEntity.setRating(totalRating / (numberOfRatings + 1)); // Tính trung bình cộng mới

        companyRepository.save(companyEntity);

        // Lưu thông báo cho công ty
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setUser(companyEntity);
        notificationEntity.setContent(applicantEntity.getFirstName() + " " + applicantEntity.getLastName() + " has been rating your company " + rateCompanyDTO.getRate());
        notificationRepository.save(notificationEntity);

        rateCompanyRepository.save(rateCompanyEntity);
        responseDTO.setData(rateCompanyEntity);
        return responseDTO;
    }

    @Override
    public void deleteRate(Long id) {
        RateCompanyEntity rateCompanyEntity = rateCompanyRepository.findById(id).orElseThrow(() -> new RuntimeException("Rate not found"));

        ApplicantEntity applicantEntity = rateCompanyEntity.getApplicant();
        CompanyEntity companyEntity = rateCompanyEntity.getCompany();

        // Tính toán lại rating của công ty sau khi xóa một đánh giá
        Double oldRating = companyEntity.getRating();
        int numberOfRatings = companyEntity.getRateCompanyEntities().size();

        // Nếu có đánh giá (trừ 1 vì đánh giá này sẽ bị xóa)
        if (numberOfRatings > 1) {
            // Tính lại rating của công ty
            Double totalRating = oldRating * numberOfRatings - rateCompanyEntity.getRate().getValue();
            companyEntity.setRating(totalRating / (numberOfRatings - 1)); // Tính lại trung bình
        } else {
            // Nếu chỉ còn 1 đánh giá, thì rating sẽ là 0
            companyEntity.setRating(0.0);
        }

        // Cập nhật lại thông tin rating trong database
        companyRepository.save(companyEntity);

        // Xóa đánh giá từ bảng RateCompanyEntity
        rateCompanyRepository.deleteById(id);
    }
}
