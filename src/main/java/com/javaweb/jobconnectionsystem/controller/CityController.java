package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.CityEntity;
import com.javaweb.jobconnectionsystem.entity.ProvinceEntity;
import com.javaweb.jobconnectionsystem.entity.WardEntity;
import com.javaweb.jobconnectionsystem.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;  

    // Endpoint để lấy tỉnh theo ProvinceID
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<ProvinceEntity> getProvinceByCityId(@PathVariable Long provinceId) {
        try {
            ProvinceEntity province = cityService.findProvince(provinceId);
            if (province == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(province, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Trường hợp có lỗi
        }
    }
    @PostMapping()
    public ResponseEntity<CityEntity> createCity(@RequestBody CityEntity city) {
//        try {
//            CityEntity newCity = cityService.addCity(city, provinceId);
//            return new ResponseEntity<>(newCity, HttpStatus.CREATED);  // Trả về thành phố mới thêm với status CREATED
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Trường hợp có lỗi
//        }
        CityEntity savedCity = cityService.addCity(city);
        return ResponseEntity.ok(savedCity);
    }

    @GetMapping("/{cityId}/wards")
    public ResponseEntity<List<WardEntity>> getWardsByCityId(@PathVariable Long cityId) {
        try {
            List<WardEntity> wards = cityService.findWard(cityId);
            if (wards.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(wards, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Nếu có lỗi
        }
    }
}
