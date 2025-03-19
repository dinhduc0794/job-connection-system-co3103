package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.model.location.ProvinceDTO;
import com.javaweb.jobconnectionsystem.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping("/public/locations")
    public ResponseEntity<?> getLocations() {
        try {
            List<ProvinceDTO> provinces = provinceService.findAllLocations();

            if (provinces.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(provinces, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
