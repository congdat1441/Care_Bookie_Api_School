package com.spring.carebookie.dto.save;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSaveDto {

    private String firstName;

    private String lastName;

    private String birthDay;

    @NotBlank
    private String email;

    private int gender;

    @NotBlank
    private String phone;

    private String address;

    @NotBlank
    private String password;

    private String startWorkingDate;

    private String hospitalId;

    private boolean isDoctor;

}
