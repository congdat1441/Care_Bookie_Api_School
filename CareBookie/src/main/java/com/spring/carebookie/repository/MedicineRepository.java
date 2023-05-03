package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.carebookie.entity.MedicineEntity;

public interface MedicineRepository extends JpaRepository<MedicineEntity,Long> {
}
