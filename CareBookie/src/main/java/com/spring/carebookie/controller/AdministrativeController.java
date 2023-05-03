package com.spring.carebookie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.carebookie.dto.edit.BookCancelDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.service.BookService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/care-bookie/administrative")
public class AdministrativeController {

    private final BookService bookService;

    @ApiOperation("Cancel book")
    @PutMapping("/book/cancel")
    public ResponseEntity<BookResponseDto> cancelBook(@RequestBody BookCancelDto dto) {
        return ResponseEntity.ok(bookService.cancelBook(dto));
    }

    @ApiOperation("Accept book")
    @PutMapping("/book/accept")
    public ResponseEntity<BookEntity> acceptBook(@RequestParam Long bookId) {
        return ResponseEntity.ok(bookService.acceptBook(bookId));
    }

    @ApiOperation("Confirm book")
    @PutMapping("/book/confirm")
    public ResponseEntity<BookEntity> confirmBook(@RequestParam Long bookId) {
        return ResponseEntity.ok(bookService.confirmBook(bookId));
    }

}
