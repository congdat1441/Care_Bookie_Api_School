package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.carebookie.entity.WorkingDayDetailsEntity;

public interface WorkingDayDetailsRepository extends JpaRepository<WorkingDayDetailsEntity,Long> {

    List<WorkingDayDetailsEntity> findAllByHospitalId(String hospitalId);
}
