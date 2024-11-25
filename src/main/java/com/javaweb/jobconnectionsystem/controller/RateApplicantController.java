package com.javaweb.jobconnectionsystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rate-applicant")
public class RateApplicantController {

    @PostMapping
    public void rateApplicant() {
        // Đánh giá ứng viên
    }
}
