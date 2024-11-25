package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.model.dto.ProvinceDTO;
import com.javaweb.jobconnectionsystem.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping("/locations")
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

    // Endpoint để lấy danh sách các thành phố (City) theo provinceId
    @GetMapping("/provinces/{provinceId}/cities")
    public ResponseEntity<List<CityEntity>> getCitiesByProvinceId(@PathVariable Long provinceId) {
        try {
            // Gọi phương thức trong service để lấy danh sách thành phố
            List<CityEntity> cities = provinceService.findCity(provinceId);

            if (cities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(cities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint để thêm mới một tỉnh (Province)
    @PostMapping("provinces")
    public ResponseEntity<ProvinceEntity> createProvince(@RequestBody ProvinceEntity newProvince) {
        try {
            ProvinceEntity province = provinceService.addProvince(newProvince);

            return new ResponseEntity<>(province, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
