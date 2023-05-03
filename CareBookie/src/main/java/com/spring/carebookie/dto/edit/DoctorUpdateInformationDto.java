package com.spring.carebookie.dto.edit;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateInformationDto {

    private String userId;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private String address;

    private String knowledge;

    private String speciality;

    private String startWorkingDate;

    private String information;

    private String status;
}
