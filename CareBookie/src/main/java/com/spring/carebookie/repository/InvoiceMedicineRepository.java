package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.InvoiceMedicineEntity;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;

public interface InvoiceMedicineRepository extends JpaRepository<InvoiceMedicineEntity,Long> {


}
