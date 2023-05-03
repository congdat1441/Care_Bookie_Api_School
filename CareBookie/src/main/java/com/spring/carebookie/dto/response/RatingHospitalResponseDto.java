package com.spring.carebookie.dto.response;

import java.util.List;

import com.spring.carebookie.entity.RatingHospitalEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingHospitalResponseDto {

    private Double star;

    private List<RatingHospitalEntity> comments;

}
