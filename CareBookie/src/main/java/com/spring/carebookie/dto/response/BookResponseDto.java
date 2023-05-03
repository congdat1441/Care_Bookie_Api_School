package com.spring.carebookie.dto.response;

import java.util.List;

import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.entity.ServiceEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private BookEntity bookInformation;

    private List<ServiceEntity> services;

    private List<InvoiceResponseDto> invoiceShares;

}
