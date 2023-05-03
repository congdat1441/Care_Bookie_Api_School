package com.spring.carebookie.dto.save;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalSaveDto {

    private String hospitalName;

    private String adminId;

    private Boolean isRate;

    private Boolean isPublicPrice;

    private Boolean isChoosenDoctor;

    private String address;

    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private String information;

    private String imageUrl;
}
