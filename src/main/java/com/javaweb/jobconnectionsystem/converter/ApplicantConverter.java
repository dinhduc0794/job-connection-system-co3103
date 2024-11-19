package com.javaweb.jobconnectionsystem.converter;

import com.javaweb.jobconnectionsystem.entity.*;

import com.javaweb.jobconnectionsystem.model.dto.ApplicantDTO;
import com.javaweb.jobconnectionsystem.repository.WardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ApplicantConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WardRepository wardRepository;

    public ApplicantEntity toApplicantEntity(ApplicantDTO applicantDTO) {
        ApplicantEntity applicantEntity = modelMapper.map(applicantDTO, ApplicantEntity.class);
        List<Long> wardIds = applicantDTO.getWardIds();
        if (wardIds != null && !wardIds.isEmpty()) {
            for (Long id : wardIds) {
              WardEntity  wardEntity = wardRepository.findById(id).get();
                // Ví dụ thêm WardEntity vào ApplicantEntity (giả sử applicantEntity đã được khởi tạo)
                applicantEntity.getWards().add(wardEntity); // Cần phương thức `addWard` trong `ApplicantEntity`
            }
        }
        List<String> phoneIds = applicantDTO.getPhoneNumberIds();
        if( phoneIds != null && !phoneIds.isEmpty()){
            for(String id : phoneIds){
                PhoneNumberEntity phoneNumber = new PhoneNumberEntity();
                phoneNumber.setPhoneNumber(id);
                phoneNumber.setUser(applicantEntity);
                applicantEntity.getPhoneNumbers().add(phoneNumber);
            }
        }
        List<String> emailIds = applicantDTO.getEmailIds();
        if( emailIds != null && !emailIds.isEmpty()){
            for(String id : emailIds){
                EmailEntity email = new EmailEntity();
                email.setEmail(id);
                email.setUser(applicantEntity);
                applicantEntity.getEmails().add(email);
            }

        }
        List<Long> notificationIds = applicantDTO.getNotificationIds();
        if (notificationIds != null && !notificationIds.isEmpty()) {
            for (Long id : notificationIds) {
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.setId(id);
                if (notificationEntity != null) {
                    applicantEntity.getNotifications().add(notificationEntity);
                }
            }
        }

       return  applicantEntity;
     }

}
