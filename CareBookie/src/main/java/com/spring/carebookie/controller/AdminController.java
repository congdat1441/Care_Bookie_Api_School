package com.spring.carebookie.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.common.constants.UserRole;
import com.spring.carebookie.dto.response.EmployeeResponseDto;
import com.spring.carebookie.dto.save.AdministrativeSaveDto;
import com.spring.carebookie.dto.save.DoctorSaveDto;
import com.spring.carebookie.dto.save.EmployeeSaveDto;
import com.spring.carebookie.dto.save.HospitalSaveDto;
import com.spring.carebookie.dto.save.UserSaveDto;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.service.HospitalService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/admin")
public class AdminController {

    private final UserService userService;

    private final HospitalService hospitalService;

    @ApiOperation("Create a doctor")
    @PostMapping("/create/doctor")
    public ResponseEntity<UserEntity> saveDoctor(@Valid @RequestBody DoctorSaveDto dto) {
        return ResponseEntity.ok(userService.saveDoctor(dto));
    }

    @ApiOperation("Create a administrative")
    @PostMapping("/create/administrative")
    public ResponseEntity<UserEntity> saveAdministrative(@Valid @RequestBody AdministrativeSaveDto dto) {
        return ResponseEntity.ok(userService.saveAdministrative(dto));
    }

    @ApiOperation("Create a doctor or administrative")
    @PostMapping("/create/employee")
    public ResponseEntity<UserEntity> saveEmployee(@Valid @RequestBody EmployeeSaveDto dto) {
        return ResponseEntity.ok(userService.saveEmployee(dto));
    }

    @ApiOperation("Get all employee")
    @GetMapping("/employees/{hospitalId}")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployee(@PathVariable String hospitalId) {
        return ResponseEntity.ok(userService.getAllEmployeeByHospitalId(hospitalId));
    }
}
