package com.spring.carebookie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.carebookie.entity.InvoiceServiceEntity;

public interface InvoiceServiceRepository extends JpaRepository<InvoiceServiceEntity,Long> {
}
