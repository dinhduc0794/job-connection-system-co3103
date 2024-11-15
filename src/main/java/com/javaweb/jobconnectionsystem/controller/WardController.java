package com.javaweb.jobconnectionsystem.controller;

import com.javaweb.jobconnectionsystem.entity.WardEntity;
import com.javaweb.jobconnectionsystem.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wards")  // Đặt đường dẫn cho API là "/api/wards"
public class WardController {

    @Autowired
    private WardService wardService;  // Inject WardService vào controller

    // Endpoint để lấy danh sách các phường/xã của một thành phố theo cityId
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<WardEntity>> getWardsByCityId(@PathVariable Long cityId) {
        try {
            List<WardEntity> wards = wardService.findWardbyCity(cityId);
            if (wards.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(wards, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint để thêm mới một phường/xã
    @PostMapping
    public ResponseEntity<WardEntity> createWard(@RequestBody WardEntity newWard) {
        try {
            WardEntity ward = wardService.AddWard(newWard);
            return new ResponseEntity<>(ward, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
