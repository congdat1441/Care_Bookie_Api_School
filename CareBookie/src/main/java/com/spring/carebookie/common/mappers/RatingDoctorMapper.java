package com.spring.carebookie.common.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.spring.carebookie.dto.save.RatingDoctorSaveDto;
import com.spring.carebookie.entity.RatingDoctorEntity;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingDoctorMapper {

    RatingDoctorMapper INSTANCE = Mappers.getMapper(RatingDoctorMapper.class);

    RatingDoctorEntity convertDtoToEntity(RatingDoctorSaveDto dto);
}
