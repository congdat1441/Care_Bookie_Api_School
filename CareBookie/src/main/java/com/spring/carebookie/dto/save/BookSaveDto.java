package com.spring.carebookie.dto.save;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveDto {

    private String userId;

    private String hospitalId;

    private String doctorId;

    private String date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateExamination;

    private String session;

    private String symptom;

    private boolean isShareInvoice;

    private String name;

    private Integer age;

    private String gender;

    private List<Long> services;

    private List<Long> invoices;

}
