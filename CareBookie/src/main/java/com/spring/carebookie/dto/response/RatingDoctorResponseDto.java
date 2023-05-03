package com.spring.carebookie.dto.response;

import java.util.List;

import com.spring.carebookie.entity.RatingDoctorEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDoctorResponseDto {
    private Double star;

    private List<RatingDoctorEntity> comments;
}
