package com.spring.carebookie.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.save.HospitalSaveDto;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.service.HospitalService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/super-admin")
public class SupperAdminController {

    private final HospitalService hospitalService;

    private final UserService userService;

    @ApiOperation("Create an account for hospital")
    @PostMapping("/hospital/save")
    public ResponseEntity<HospitalEntity> save(@RequestBody HospitalSaveDto dto) {
        return ResponseEntity.ok(hospitalService.saveHospital(dto));
    }

    @ApiOperation("Get all users include user, employee, admin")
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
       return ResponseEntity.ok( userService.getAllUsers());
    }

    @ApiOperation("Get all users (patients)")
    @GetMapping("/users/patient")
    public ResponseEntity<List<UserEntity>> getAllPatients() {
        return ResponseEntity.ok( userService.getAllPatients());
    }
}
