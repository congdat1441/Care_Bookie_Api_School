package com.spring.carebookie.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.DoctorUpdateInformationDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.service.BookService;
import com.spring.carebookie.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/doctor")
public class DoctorController {

    private final UserService userService;

    private final BookService bookService;

    @ApiOperation("Get all book doctorId and status is ACCEPT")
    @GetMapping("/book/accept")
    public ResponseEntity<List<BookResponseDto>> getAllBookAcceptedByDoctorId(@RequestParam String doctorId) {
        return ResponseEntity.ok(bookService.getAllBookAcceptByDoctorId(doctorId));
    }

    @ApiOperation("Get all book doctorId and status is ACCEPT")
    @GetMapping("/book/confirm")
    public ResponseEntity<List<BookResponseDto>> getAllBookConfirmByDoctorId(@RequestParam String doctorId) {
        return ResponseEntity.ok(bookService.getAllBookConfirmByDoctorId(doctorId));
    }

    @ApiOperation("Update doctor information")
    @PutMapping("/update/information")
    public ResponseEntity<UserEntity> updateInformation(@RequestBody DoctorUpdateInformationDto dto) {
        return ResponseEntity.ok(userService.updateDoctor(dto));
    }

}
