package com.spring.carebookie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.BookCancelDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.save.BookSaveDto;
import com.spring.carebookie.dto.save.RatingDoctorSaveDto;
import com.spring.carebookie.dto.save.RatingHospitalSaveDto;
import com.spring.carebookie.entity.RatingDoctorEntity;
import com.spring.carebookie.entity.RatingHospitalEntity;
import com.spring.carebookie.service.BookService;
import com.spring.carebookie.service.CommonService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/care-bookie/user")
@CrossOrigin("*")
public class UserController {

    private CommonService commonService;

    private BookService bookService;

    @ApiOperation("Create new comment for doctor")
    @PostMapping("comment/doctor")
    public ResponseEntity<RatingDoctorEntity> saveCommentDoctor(@RequestBody RatingDoctorSaveDto dto) {
        return ResponseEntity.ok(commonService.saveRatingDoctor(dto));
    }

    @ApiOperation("Create new comment for hospital")
    @PostMapping("comment/hospital")
    public ResponseEntity<RatingHospitalEntity> saveCommentHospital(@RequestBody RatingHospitalSaveDto dto) {
        return ResponseEntity.ok(commonService.saveRatingHospital(dto));
    }

    @ApiOperation("Create a book")
    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> createNewBook(@RequestBody BookSaveDto dto) {
        return ResponseEntity.ok(bookService.saveBook(dto));
    }

    @ApiOperation("Cancel book")
    @PutMapping("/book/cancel")
    public ResponseEntity<?> cancelBook(@RequestBody BookCancelDto dto) {
        return ResponseEntity.ok(bookService.cancelBook(dto));
    }
}
