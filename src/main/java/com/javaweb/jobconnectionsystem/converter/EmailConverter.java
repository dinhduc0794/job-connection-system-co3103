//package com.javaweb.jobconnectionsystem.converter;
//
//import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
//import com.javaweb.jobconnectionsystem.entity.EmailEntity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class EmailConverter {
//    public List<EmailEntity> toEmailEntity(List<String> emails, CompanyEntity companyEntity) {
//        if (emails == null || emails.isEmpty()) {
//            return Collections.emptyList();
//        }
//        List<EmailEntity> emailEntities = new ArrayList<>();
//        for(String email : emails) {
//            EmailEntity emailEntity = new EmailEntity();
//            emailEntity.setEmail(email);
//            emailEntity.setUser(companyEntity);
//
//        }
//    }
//
//}
