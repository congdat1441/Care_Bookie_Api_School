package com.spring.carebookie.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.spring.carebookie.dto.save.RatingHospitalSaveDto;
import com.spring.carebookie.entity.RatingHospitalEntity;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingHospitalMapper {
    RatingHospitalMapper INSTANCE = Mappers.getMapper(RatingHospitalMapper.class);

    RatingHospitalEntity convertDtoToEntity(RatingHospitalSaveDto dto);
}
